package ml.rk585.jetmusic.ui.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import ml.rk585.jetmusic.core.domain.interactors.FetchPlaylist
import ml.rk585.jetmusic.core.domain.observers.ObservePagedPlaylist
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import javax.inject.Inject

@HiltViewModel
internal class PlaylistViewModel @Inject constructor(
    private val fetchPlaylist: FetchPlaylist,
    private val playlistPager: ObservePagedPlaylist,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val playlistId = savedStateHandle.getStateFlow("id", "")

    private val _playlistInfo = MutableStateFlow<PlaylistInfo?>(null)
    val playlistInfo: StateFlow<PlaylistInfo?> = _playlistInfo.asStateFlow()
    val playlistPagerFlow = playlistPager.flow.cachedIn(viewModelScope)

    init {
        fetchPlaylist(false)
    }

    fun refresh() {
        fetchPlaylist(true)
    }

    private fun fetchPlaylist(forceRefresh: Boolean) {
        viewModelScope.launch {
            fetchPlaylist(FetchPlaylist.Params(playlistId.value, forceRefresh))
                .catch { logcat { it.asLog() } }
                .collectLatest { info ->
                    _playlistInfo.update { info }
                }
            playlistPager(ObservePagedPlaylist.Params(playlistId.value))
        }
    }
}
