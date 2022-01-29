package ml.rk585.jetmusic.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.data.model.SearchQuery
import ml.rk585.jetmusic.data.model.SearchType
import ml.rk585.jetmusic.data.repo.MusicRepo
import org.schabi.newpipe.extractor.InfoItem
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepo: MusicRepo
) : ViewModel() {

    private val searchQuery: MutableStateFlow<SearchQuery> = MutableStateFlow(SearchQuery())
    val query: StateFlow<SearchQuery> = searchQuery.asStateFlow()

    private val _state = MutableStateFlow(emptyList<InfoItem>())
    val state: StateFlow<List<InfoItem>> = _state.asStateFlow()

    fun updateQuery(query: String) {
        if (query != searchQuery.value.query) {
            searchQuery.update {
                searchQuery.value.copy(query = query)
            }
        }
    }

    fun updateSearchType(type: SearchType) {
        if (type != searchQuery.value.type) {
            searchQuery.update {
                searchQuery.value.copy(type = type)
            }
        }
    }

    init {
        viewModelScope.launch {
            searchQuery.debounce(300)
                .filter { it.query.isNotBlank() && it.query.isNotEmpty() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    _state.update { musicRepo.search(query) }
                }
        }
    }
}
