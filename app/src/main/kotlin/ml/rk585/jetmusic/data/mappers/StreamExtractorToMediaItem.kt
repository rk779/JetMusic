package ml.rk585.jetmusic.data.mappers

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import logcat.logcat
import org.schabi.newpipe.extractor.stream.StreamExtractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamExtractorToMediaItem @Inject constructor() : Mapper<StreamExtractor, MediaItem> {

    override suspend fun map(from: StreamExtractor): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setMediaUri(getBestStreamUri(from))
            .setTitle(from.name)
            .setArtist(from.uploaderName)
            .setArtworkUri(from.thumbnailUrl.toUri())
            .build()

        return MediaItem.Builder()
            .setMediaId(from.id)
            .setMediaMetadata(mediaMetadata)
            .build()
    }

    companion object {
        private fun getBestStreamUri(streamExtractor: StreamExtractor?): Uri {
            val streamUri = streamExtractor?.hlsUrl ?: streamExtractor?.dashMpdUrl
            ?: streamExtractor!!.audioStreams.run { this[this.size - 1] }.url
            logcat { streamUri }
            return streamUri.toUri()
        }
    }
}