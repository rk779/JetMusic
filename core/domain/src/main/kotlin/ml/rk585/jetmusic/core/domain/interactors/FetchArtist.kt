package ml.rk585.jetmusic.core.domain.interactors

import ml.rk585.jetmusic.core.base.extensions.fetch
import ml.rk585.jetmusic.core.data.repositories.artist.ArtistStore
import ml.rk585.jetmusic.core.domain.ResultInteractor
import org.schabi.newpipe.extractor.channel.ChannelInfo
import javax.inject.Inject

class FetchArtist @Inject constructor(
    private val artistStore: ArtistStore
) : ResultInteractor<FetchArtist.Params, ChannelInfo>() {

    override suspend fun doWork(params: Params): ChannelInfo {
        return artistStore.fetch(params.id, params.forceRefresh)
    }

    data class Params(
        val id: String,
        val forceRefresh: Boolean = false
    )
}
