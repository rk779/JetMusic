package ml.rk585.jetmusic.ui.screens.playlist

import androidx.compose.runtime.Immutable
import ml.rk585.jetmusic.data.model.StreamsCountDuration
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@Immutable
data class PlaylistViewState(
    val items: List<StreamInfoItem> = emptyList(),
    val playerLoading: Boolean = false,
    val streamsCountDuration: StreamsCountDuration? = null
) {
    companion object {
        val Empty = PlaylistViewState()
    }
}
