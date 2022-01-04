package cf.rk585.vivace.core.domain.interactors

import cf.rk585.vivace.core.base.util.CoroutineDispatchers
import cf.rk585.vivace.core.domain.SuspendingWorkInteractor
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.ServiceList.YouTube
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

class SearchMusic @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : SuspendingWorkInteractor<SearchMusic.Params, List<StreamInfoItem>>() {

    override suspend fun doWork(params: SearchMusic.Params): List<StreamInfoItem> {
        return withContext(dispatchers.network) {
            val extractor = YouTube.getSearchExtractor(
                params.query,
                listOf(
                    YoutubeSearchQueryHandlerFactory.MUSIC_SONGS,
                    YoutubeSearchQueryHandlerFactory.MUSIC_VIDEOS,
                ),
                null
            )
            extractor.fetchPage()
            extractor.initialPage.items.map { it as StreamInfoItem }
        }
    }

    data class Params(val query: String)
}
