package ml.rk585.jetmusic.core.data.repositories.playlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ml.rk585.jetmusic.core.base.extensions.toPage
import ml.rk585.jetmusic.core.base.util.ExtractorHelper
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

class PlaylistPagingSource @Inject constructor(
    private val extractorHelper: ExtractorHelper
) {
    operator fun invoke(id: String) = object : PagingSource<Page, StreamInfoItem>() {
        override suspend fun load(params: LoadParams<Page>): LoadResult<Page, StreamInfoItem> {
            return try {
                if (params.key != null) {
                    extractorHelper.getPlaylist(
                        id = id,
                        nextPage = params.key!!
                    ).toPage()
                } else {
                    extractorHelper.getPlaylist(id).toPage()
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Page, StreamInfoItem>): Page? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey
            }
        }
    }
}
