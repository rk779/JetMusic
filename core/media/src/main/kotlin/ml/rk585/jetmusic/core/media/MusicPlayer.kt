package ml.rk585.jetmusic.core.media

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.core.media.model.PlaybackProgress
import ml.rk585.jetmusic.core.media.model.PlayerState
import ml.rk585.jetmusic.core.media.util.toMediaItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

interface MusicPlayer {
    val isPlaying: StateFlow<Boolean>
    val currentMediaItem: StateFlow<MediaItem>
    val playbackProgress: StateFlow<PlaybackProgress>
    val playbackState: StateFlow<Int>
    val repeatMode: StateFlow<Int>
    val shuffleModeEnabled: StateFlow<Boolean>

    val playerState: StateFlow<PlayerState>

    fun hasNextMediaItem(): Boolean
    fun hasPreviousMediaItem(): Boolean

    fun pauseOrResume()
    fun play(streamInfoItem: StreamInfoItem)
    fun seekTo(positionMs: Long)
    fun seekToNext()
    fun seekToPrevious()
    fun stop()

    fun toggleRepeatMode()
    fun toggleShuffleMode()

    fun initializeController()
    fun releaseController()
}

class MusicPlayerImpl(
    private val context: Context,
    private val serviceComponent: ComponentName,
    private val coroutineScope: CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope
) : MusicPlayer, CoroutineScope by coroutineScope {
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val mediaController: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentMediaItem = MutableStateFlow(MediaItem.EMPTY)
    override val currentMediaItem: StateFlow<MediaItem> = _currentMediaItem.asStateFlow()

    private var playbackProgressJob: Job = Job()
    private val _playbackProgress = MutableStateFlow(PlaybackProgress())
    override val playbackProgress: StateFlow<PlaybackProgress> = _playbackProgress.asStateFlow()

    private val _playbackState = MutableStateFlow(Player.STATE_IDLE)
    override val playbackState: StateFlow<Int> = _playbackState.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    override val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private val _shuffleModeEnabled = MutableStateFlow(false)
    override val shuffleModeEnabled: StateFlow<Boolean> = _shuffleModeEnabled.asStateFlow()

    override val playerState: StateFlow<PlayerState> = combine(
        _currentMediaItem,
        _isPlaying,
        _repeatMode,
        _shuffleModeEnabled,
        ::PlayerState
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerState.Empty
    )

    override fun hasNextMediaItem(): Boolean {
        return mediaController?.hasNextMediaItem() ?: false
    }

    override fun hasPreviousMediaItem(): Boolean {
        return mediaController?.hasPreviousMediaItem() ?: false
    }

    override fun pauseOrResume() {
        when (mediaController?.isPlaying) {
            true -> mediaController?.pause()
            else -> mediaController?.play()
        }
    }

    override fun play(streamInfoItem: StreamInfoItem) {
        val mediaItem = streamInfoItem.toMediaItem()
        mediaController?.setMediaItem(mediaItem)
        mediaController?.prepare()
    }

    override fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
    }

    override fun seekToNext() {
        mediaController?.seekToNext()
    }

    override fun seekToPrevious() {
        mediaController?.seekToPrevious()
    }

    override fun stop() {
        mediaController?.stop()
    }

    override fun toggleRepeatMode() {
        mediaController?.repeatMode = when (mediaController?.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }
    }

    override fun toggleShuffleMode() {
        mediaController?.let { controller ->
            controller.shuffleModeEnabled = !controller.shuffleModeEnabled
        }
    }

    override fun initializeController() {
        controllerFuture = MediaController.Builder(
            context, SessionToken(context, serviceComponent)
        )
            .buildAsync()
        controllerFuture.addListener(::setController, MoreExecutors.directExecutor())
    }

    override fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }

    private fun setController() {
        val controller = this.mediaController ?: return
        controller.addListener(playerListener)
        controller.playWhenReady = true
        startPlaybackProgress(controller)
    }

    private val playerListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            _currentMediaItem.update { mediaItem ?: MediaItem.EMPTY }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.update { isPlaying }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            _playbackState.update { playbackState }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            _repeatMode.update { repeatMode }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _shuffleModeEnabled.update { shuffleModeEnabled }
        }
    }

    private fun startPlaybackProgress(mediaController: MediaController) {
        launch {
            combine(currentMediaItem, playbackState, ::Pair)
                .collectLatest { (mediaItem, playerState) ->
                    playbackProgressJob.cancel()
                    if (mediaItem == MediaItem.EMPTY || playerState == Player.STATE_ENDED) {
                        return@collectLatest
                    }
                    if (mediaController.isPlaying && playerState != Player.STATE_BUFFERING) {
                        startPlaybackProgressUpdateJob(mediaController)
                    }
                }
        }
    }

    private fun startPlaybackProgressUpdateJob(mediaController: MediaController) {
        playbackProgressJob = launch {
            while (isActive) {
                _playbackProgress.update { playbackProgress ->
                    playbackProgress.copy(
                        total = mediaController.duration.coerceAtLeast(1),
                        position = mediaController.currentPosition,
                        buffered = mediaController.bufferedPosition
                    )
                }
                delay(1000)
            }
        }
    }
}
