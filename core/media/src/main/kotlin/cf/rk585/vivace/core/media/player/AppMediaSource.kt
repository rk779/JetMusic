package cf.rk585.vivace.core.media.player

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import org.schabi.newpipe.extractor.stream.StreamInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppMediaSource @Inject constructor() {
    var mediaMetaDataSongs: List<MediaMetadataCompat> = emptyList()
    var mediaSongs: List<StreamInfo> = emptyList()
        private set
    private val onReadyListeners = mutableListOf<OnReadyListener>()

    private var state: MusicSourceState =
        MusicSourceState.CREATED
        set(value) {
            if (value == MusicSourceState.INITIALIZED || value == MusicSourceState.ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(isReady)
                    }
                }
            } else {
                field = value
            }
        }

    private val isReady: Boolean
        get() = state == MusicSourceState.INITIALIZED

    fun setSongs(data: List<StreamInfo>) {
        state = MusicSourceState.INITIALIZING
        mediaSongs = data
        mediaMetaDataSongs = data.map { item ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, item.id)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, item.uploaderName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, item.name)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, item.uploaderName)
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                    item.audioStreams.run { this[this.size - 1] }.url
                )
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, item.thumbnailUrl)
                .build()
        }
        state = MusicSourceState.INITIALIZED
    }

    fun asMediaSource(dataSource: OkHttpDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        mediaMetaDataSongs.forEach { metaData ->
            val mediaItem = MediaItem.fromUri(
                metaData.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
            )
            val mediaSource =
                ProgressiveMediaSource.Factory(dataSource).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = mediaMetaDataSongs.map { metadata ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(metadata.description.mediaId)
            .setTitle(metadata.description.title)
            .setSubtitle(metadata.description.subtitle)
            .setIconUri(metadata.description.iconUri)
            .setMediaUri(metadata.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()

    fun whenReady(listener: OnReadyListener): Boolean {
        return if (state == MusicSourceState.CREATED || state == MusicSourceState.INITIALIZING) {
            onReadyListeners += listener
            false
        } else {
            listener(isReady)
            true
        }
    }

    fun refresh() {
        onReadyListeners.clear()
        state = MusicSourceState.CREATED
    }
}

typealias OnReadyListener = (Boolean) -> Unit

enum class MusicSourceState {
    CREATED,
    INITIALIZING,
    INITIALIZED,
    ERROR
}
