package ml.rk585.jetmusic.ui.artist

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
import ml.rk585.jetmusic.core.domain.interactors.FetchArtist
import ml.rk585.jetmusic.core.domain.observers.ObservePagedArtist
import org.schabi.newpipe.extractor.channel.ChannelInfo
import javax.inject.Inject

@HiltViewModel
internal class ArtistViewModel @Inject constructor(
    private val fetchArtist: FetchArtist,
    private val artistPager: ObservePagedArtist,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val channelId = savedStateHandle.getStateFlow("channelId", "")

    private val _channelInfo = MutableStateFlow<ChannelInfo?>(null)
    val channelInfo: StateFlow<ChannelInfo?> = _channelInfo.asStateFlow()
    val artistPagerFlow = artistPager.flow.cachedIn(viewModelScope)

    init {
        fetchArtist(false)
    }

    fun refresh() {
        fetchArtist(true)
    }

    private fun fetchArtist(forceRefresh: Boolean) {
        viewModelScope.launch {
            fetchArtist(FetchArtist.Params(channelId.value, forceRefresh))
                .catch { logcat { it.asLog() } }
                .collectLatest { info ->
                    _channelInfo.update { info }
                }
            artistPager(ObservePagedArtist.Params(channelId.value))
        }
    }
}
