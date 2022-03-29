package ml.rk585.jetmusic.core.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ml.rk585.jetmusic.core.data.repositories.artist.ArtistPagingSource
import ml.rk585.jetmusic.core.domain.PagingInteractor
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

class ObservePagedArtist @Inject constructor(
    private val artistPagingSource: ArtistPagingSource
) : PagingInteractor<ObservePagedArtist.Params, StreamInfoItem>() {

    override fun createObservable(params: Params): Flow<PagingData<StreamInfoItem>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = { artistPagingSource(params.channelId) }
        ).flow
    }

    data class Params(
        val channelId: String,
        override val pagingConfig: PagingConfig = DEFAULT_PAGING_CONFIG
    ) : Parameters<StreamInfoItem>
}
