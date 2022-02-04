package ml.rk585.jetmusic.ui.screens.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.data.repo.MusicRepo
import ml.rk585.jetmusic.util.toDecodedUri
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val musicRepo: MusicRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val playlistUrl = requireNotNull(savedStateHandle.get<String>("playlistUrl"))
        .toDecodedUri()

    private val _state = MutableStateFlow(emptyList<StreamInfoItem>())
    val state: StateFlow<List<StreamInfoItem>> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val items = musicRepo.getPlayList(playlistUrl)
            _state.update { items }
        }
    }
}
