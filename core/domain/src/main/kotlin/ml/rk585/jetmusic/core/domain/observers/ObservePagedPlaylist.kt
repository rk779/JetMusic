package ml.rk585.jetmusic.core.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ml.rk585.jetmusic.core.data.repositories.playlist.PlaylistPagingSource
import ml.rk585.jetmusic.core.domain.PagingInteractor
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

class ObservePagedPlaylist @Inject constructor(
    private val playlistPagingSource: PlaylistPagingSource
) : PagingInteractor<ObservePagedPlaylist.Params, StreamInfoItem>() {

    override fun createObservable(params: Params): Flow<PagingData<StreamInfoItem>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = { playlistPagingSource(params.playlistId) }
        ).flow
    }

    data class Params(
        val playlistId: String,
        override val pagingConfig: PagingConfig = DEFAULT_PAGING_CONFIG
    ) : Parameters<StreamInfoItem>
}
