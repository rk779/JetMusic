package ml.rk585.jetmusic.core.media.util

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ml.rk585.jetmusic.core.base.util.Extractor
import org.schabi.newpipe.extractor.stream.StreamInfoItem

fun StreamInfoItem.toMediaItem(): MediaItem {
    val mediaId = Extractor.extractStreamId(url) ?: throw IllegalStateException("Empty media id")
    val mediaMetadata = MediaMetadata.Builder()
        .setMediaUri(mediaId.toUri())
        .setTitle(name)
        .setArtist(uploaderName)
        .setArtworkUri(thumbnailUrl.toUri())
        .build()

    return MediaItem.Builder()
        .setMediaId(mediaId)
        .setMediaMetadata(mediaMetadata)
        .setUri(mediaId)
        .build()
}
