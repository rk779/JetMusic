package ml.rk585.jetmusic.core.base.util

import ml.rk585.jetmusic.core.base.extensions.tryOrNull
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.services.youtube.YoutubeService

object Extractor {
    private val youtubeService: YoutubeService = ServiceList.YouTube

    fun extractChannelId(url: String): String? {
        return tryOrNull { youtubeService.channelLHFactory.getId(url) }
    }

    fun extractPlaylistId(url: String): String? {
        return tryOrNull { youtubeService.playlistLHFactory.getId(url) }
    }

    fun extractStreamId(url: String): String? {
        return tryOrNull { youtubeService.streamLHFactory.getId(url) }
    }
}
