package ml.rk585.jetmusic.ui.screens.artist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.data.model.Artist
import ml.rk585.jetmusic.data.model.StreamsCountDuration
import ml.rk585.jetmusic.data.repo.MusicRepo
import ml.rk585.jetmusic.data.service.PlayerConnection
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val musicRepo: MusicRepo,
    private val playerConnection: PlayerConnection
) : ViewModel() {
    private val artistUrl = requireNotNull(handle.get<String>("artistUrl"))

    private val artist = MutableStateFlow(Artist())
    private val playerLoading = MutableStateFlow(false)

    val state = combine(
        artist,
        playerLoading,
        ::ArtistViewState
    ).map { state ->
        if (state.artist.items.isNotEmpty()) {
            state.copy(streamsCountDuration = StreamsCountDuration.from(state.artist.items))
        } else state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArtistViewState.Empty
    )

    init {
        fetchArtist(artistUrl)
    }

    private fun fetchArtist(url: String) {
        viewModelScope.launch {
            artist.update { musicRepo.getArtist(url) }
        }
    }

    fun playAll(items: List<StreamInfoItem>) {
        viewModelScope.launch {
            playerLoading.update { true }
            val mediaItems = items.map { musicRepo.getMediaItemFromUrl(it) }
            playerLoading.update { false }
            playerConnection.play(mediaItems)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerConnection.releaseMediaController()
    }
}
