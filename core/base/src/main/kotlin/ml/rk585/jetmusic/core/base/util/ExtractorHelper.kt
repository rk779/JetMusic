package ml.rk585.jetmusic.core.base.util

import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.ChannelInfo
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import org.schabi.newpipe.extractor.search.SearchInfo
import org.schabi.newpipe.extractor.services.youtube.YoutubeService
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem

class ExtractorHelper(
    private val dispatchers: CoroutineDispatchers,
    private val youtubeService: YoutubeService
) {
    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null
    ): SearchInfo {
        return withContext(dispatchers.io) {
            SearchInfo.getInfo(
                youtubeService,
                youtubeService.searchQHFactory.fromQuery(query, contentFilter, sortFilter)
            )
        }
    }

    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
        nextPage: Page
    ): InfoItemsPage<InfoItem> {
        return withContext(dispatchers.io) {
            SearchInfo.getMoreItems(
                youtubeService,
                youtubeService.searchQHFactory.fromQuery(query, contentFilter, sortFilter),
                nextPage
            )
        }
    }

    suspend fun getPlaylist(id: String): PlaylistInfo {
        return withContext(dispatchers.io) {
            PlaylistInfo.getInfo(
                youtubeService,
                youtubeService.playlistLHFactory.getUrl(id)
            )
        }
    }

    suspend fun getPlaylist(
        id: String,
        nextPage: Page
    ): InfoItemsPage<StreamInfoItem> {
        return withContext(dispatchers.io) {
            PlaylistInfo.getMoreItems(
                youtubeService,
                youtubeService.playlistLHFactory.getUrl(id),
                nextPage
            )
        }
    }

    suspend fun getChannel(id: String): ChannelInfo {
        return withContext(dispatchers.io) {
            ChannelInfo.getInfo(
                youtubeService,
                youtubeService.channelLHFactory.getUrl(id)
            )
        }
    }

    suspend fun getChannel(
        id: String,
        nextPage: Page
    ): InfoItemsPage<StreamInfoItem> {
        return withContext(dispatchers.io) {
            ChannelInfo.getMoreItems(
                youtubeService,
                youtubeService.channelLHFactory.getUrl(id),
                nextPage
            )
        }
    }

    suspend fun getStreamInfo(id: String): StreamInfo {
        return withContext(dispatchers.io) {
            StreamInfo.getInfo(
                youtubeService,
                youtubeService.streamLHFactory.getUrl(id)
            )
        }
    }
}
