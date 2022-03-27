package ml.rk585.jetmusic.core.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ml.rk585.jetmusic.core.data.repositories.search.SearchDataSource
import ml.rk585.jetmusic.core.domain.PagingInteractor
import org.schabi.newpipe.extractor.InfoItem
import javax.inject.Inject

class ObservePagedSearch @Inject constructor(
    private val searchDataSource: SearchDataSource
) : PagingInteractor<ObservePagedSearch.Params, InfoItem>() {

    override fun createObservable(params: Params): Flow<PagingData<InfoItem>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = { searchDataSource(params.searchParams) }
        ).flow
    }

    data class Params(
        val searchParams: SearchDataSource.Params,
        override val pagingConfig: PagingConfig = DEFAULT_PAGING_CONFIG
    ) : Parameters<InfoItem>
}
