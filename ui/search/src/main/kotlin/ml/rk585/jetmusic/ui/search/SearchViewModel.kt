package ml.rk585.jetmusic.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.core.data.repositories.search.SearchDataSource
import ml.rk585.jetmusic.core.domain.observers.ObservePagedSearch
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val albumsPager: ObservePagedSearch,
    private val artistsPager: ObservePagedSearch,
    private val playlistsPager: ObservePagedSearch,
    private val songsPager: ObservePagedSearch
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val albumsPagerFlow = albumsPager.flow.cachedIn(viewModelScope)
    val artistsPagerFlow = artistsPager.flow.cachedIn(viewModelScope)
    val playlistsPagerFlow = playlistsPager.flow.cachedIn(viewModelScope)
    val songsPagerFlow = songsPager.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            searchQuery.debounce(300)
                .filter { it.isNotBlank() && it.isNotEmpty() }
                .distinctUntilChanged()
                .onEach { query ->
                    search(query)
                }
                .collect()
        }
    }

    fun updateQuery(value: String) {
        searchQuery.update { value }
    }

    private fun search(query: String) {
        albumsPager(
            ObservePagedSearch.Params(
                SearchDataSource.Params(
                    query,
                    listOf(YoutubeSearchQueryHandlerFactory.MUSIC_ALBUMS)
                )
            )
        )
        artistsPager(
            ObservePagedSearch.Params(
                SearchDataSource.Params(
                    query,
                    listOf(YoutubeSearchQueryHandlerFactory.CHANNELS)
                )
            )
        )
        playlistsPager(
            ObservePagedSearch.Params(
                SearchDataSource.Params(
                    query,
                    listOf(YoutubeSearchQueryHandlerFactory.MUSIC_PLAYLISTS)
                )
            )
        )
        songsPager(
            ObservePagedSearch.Params(
                SearchDataSource.Params(
                    query,
                    listOf(
                        YoutubeSearchQueryHandlerFactory.MUSIC_SONGS,
                        YoutubeSearchQueryHandlerFactory.MUSIC_VIDEOS
                    )
                )
            )
        )
    }
}
