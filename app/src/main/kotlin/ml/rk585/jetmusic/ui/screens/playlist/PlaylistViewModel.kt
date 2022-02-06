package ml.rk585.jetmusic.ui.screens.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.data.model.StreamsCountDuration
import ml.rk585.jetmusic.data.repo.MusicRepo
import ml.rk585.jetmusic.data.service.PlayerConnection
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val musicRepo: MusicRepo,
    private val playerConnection: PlayerConnection
) : ViewModel() {
    private val playlistUrl = requireNotNull(handle.get<String>("playlistUrl"))

    private val playerLoading = MutableStateFlow(false)
    private val songs = MutableStateFlow(emptyList<StreamInfoItem>())

    val uiState: StateFlow<PlaylistViewState> = combine(
        songs,
        playerLoading,
        ::PlaylistViewState
    ).map { state ->
        if (state.items.isNotEmpty()) {
            state.copy(streamsCountDuration = StreamsCountDuration.from(state.items))
        } else state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlaylistViewState.Empty
    )

    init {
        viewModelScope.launch {
            val items = musicRepo.getPlayList(playlistUrl)
            songs.update { items }
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
