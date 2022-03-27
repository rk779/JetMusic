package ml.rk585.jetmusic.core.data.repositories.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ml.rk585.jetmusic.core.base.extensions.toPage
import ml.rk585.jetmusic.core.base.util.ExtractorHelper
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.Page
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val extractorHelper: ExtractorHelper
) {
    operator fun invoke(searchParams: Params): PagingSource<Page, InfoItem> {
        return object : PagingSource<Page, InfoItem>() {
            override fun getRefreshKey(state: PagingState<Page, InfoItem>): Page? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey
                }
            }

            override suspend fun load(params: LoadParams<Page>): LoadResult<Page, InfoItem> {
                return try {
                    if (params.key != null) {
                        extractorHelper.search(
                            query = searchParams.query,
                            contentFilter = searchParams.contentFiler,
                            sortFilter = searchParams.sortFilter,
                            nextPage = params.key!!
                        ).toPage()
                    } else {
                        extractorHelper.search(
                            query = searchParams.query,
                            contentFilter = searchParams.contentFiler,
                            sortFilter = searchParams.sortFilter
                        ).toPage()
                    }
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }
        }
    }

    data class Params(
        val query: String,
        val contentFiler: List<String> = emptyList(),
        val sortFilter: String = ""
    )
}
