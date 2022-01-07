package tech.rk585.vivace.ui.search

import androidx.compose.runtime.Immutable
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@Immutable
internal data class SearchViewState(
    val query: String = "",
    val songs: List<StreamInfoItem> = emptyList(),
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = SearchViewState()
    }
}
