package ml.rk585.jetmusic.ui.screens.artist

import androidx.compose.runtime.Immutable
import ml.rk585.jetmusic.data.model.Artist
import ml.rk585.jetmusic.data.model.StreamsCountDuration

@Immutable
data class ArtistViewState(
    val artist: Artist = Artist(),
    val playerLoading: Boolean = false,
    val streamsCountDuration: StreamsCountDuration? = null
) {
    companion object {
        val Empty = ArtistViewState()
    }
}
