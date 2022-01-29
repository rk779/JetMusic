package ml.rk585.jetmusic.data.repo

import kotlinx.coroutines.withContext
import ml.rk585.jetmusic.data.model.SearchQuery
import ml.rk585.jetmusic.data.model.SearchType
import ml.rk585.jetmusic.util.CoroutineDispatchers
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.ServiceList.YouTube
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory
import javax.inject.Inject

class MusicRepo @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun search(searchQuery: SearchQuery): List<InfoItem> {
        return withContext(dispatchers.network) {
            val contentFilter = when (searchQuery.type) {
                SearchType.ALBUMS -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_ALBUMS)
                SearchType.ARTISTS -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_ARTISTS)
                SearchType.MUSIC -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_SONGS)
                SearchType.PLAYLISTS -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_PLAYLISTS)
            }
            val extractor = YouTube.getSearchExtractor(
                searchQuery.query,
                contentFilter,
                null
            )
            extractor.fetchPage()
            extractor.initialPage.items ?: emptyList()
        }
    }
}
