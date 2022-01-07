package tech.rk585.vivace.ui.nowPlaying

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cf.rk585.vivace.core.media.services.MediaPlayerService
import cf.rk585.vivace.core.media.services.MediaPlayerService.Companion.MEDIA_ROOT_ID
import cf.rk585.vivace.core.media.services.MediaPlayerServiceConnection
import cf.rk585.vivace.core.media.util.currentPosition
import cf.rk585.vivace.core.media.util.isPlayEnabled
import cf.rk585.vivace.core.media.util.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {
    val currentPlayingSong = serviceConnection.currentPlayingSong
    var showFullScreen by mutableStateOf(false)
    var currentPlaybackPosition by mutableStateOf(0L)

    private val playbackState = serviceConnection.playbackState
    val songIsPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    private val currentSongDuration: Long
        get() = MediaPlayerService.currentDuration

    val currentSongProgress: Float
        get() {
            if (currentSongDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentSongDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = formatLong(currentPlaybackPosition)

    val currentSongFormattedDuration: String
        get() = formatLong(currentSongDuration)

    fun playSongs(songs: List<StreamInfo>, currentSong: StreamInfo) {
        serviceConnection.playSongs(songs)
        if (currentSong.id == currentPlayingSong.value?.id) {
            if (songIsPlaying) {
                serviceConnection.transportControls.pause()
            } else {
                serviceConnection.transportControls.play()
            }
        } else {
            serviceConnection.transportControls.playFromMediaId(currentSong.id, null)
        }
    }

    fun togglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> {
                serviceConnection.transportControls.pause()
            }

            playbackState.value?.isPlayEnabled == true -> {
                serviceConnection.transportControls.play()
            }
        }
    }

    fun stopPlayback() {
        serviceConnection.transportControls.stop()
    }

    fun fastForward() {
        serviceConnection.fastForward()
    }

    fun rewind() {
        serviceConnection.rewind()
    }

    /**
     * @param value 0.0 to 1.0
     */
    fun seekToFraction(value: Float) {
        serviceConnection.transportControls.seekTo(
            (currentSongDuration * value).toLong()
        )
    }

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(1000)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(value: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(value)
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}
