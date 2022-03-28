package ml.rk585.jetmusic.core.domain.interactors

import ml.rk585.jetmusic.core.base.extensions.fetch
import ml.rk585.jetmusic.core.data.repositories.playlist.PlaylistStore
import ml.rk585.jetmusic.core.domain.ResultInteractor
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import javax.inject.Inject

class FetchPlaylist @Inject constructor(
    private val playlistStore: PlaylistStore
) : ResultInteractor<FetchPlaylist.Params, PlaylistInfo>() {

    override suspend fun doWork(params: Params): PlaylistInfo {
        return playlistStore.fetch(params.id, params.forceRefresh)
    }

    data class Params(
        val id: String,
        val forceRefresh: Boolean = false
    )
}
