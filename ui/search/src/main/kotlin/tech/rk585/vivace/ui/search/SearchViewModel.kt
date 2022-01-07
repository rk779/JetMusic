package tech.rk585.vivace.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cf.rk585.vivace.core.base.util.CoroutineDispatchers
import cf.rk585.vivace.core.base.util.ObservableLoadingCounter
import cf.rk585.vivace.core.domain.interactors.SearchMusic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val searchMusic: SearchMusic,
    val dispatchers: CoroutineDispatchers
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val loadingState = ObservableLoadingCounter()

    val state: StateFlow<SearchViewState> = combine(
        searchQuery,
        searchMusic.flow,
        loadingState.observable,
        ::SearchViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchViewState.Empty
    )

    init {
        viewModelScope.launch {
            searchQuery.debounce(300)
                .filter { it.isNotBlank() || it.isNotEmpty() }
                .onEach { query ->
                    val job = launch {
                        loadingState.addLoader()
                        searchMusic(SearchMusic.Params(query))
                    }
                    job.invokeOnCompletion { loadingState.removeLoader() }
                    job.join()
                }
                .catch { error -> Log.e("SearchMusic", ": ", error) }
                .collect()
        }
    }

    fun updateSearch(query: String) {
        searchQuery.update { query }
    }
}
