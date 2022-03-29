package ml.rk585.jetmusic.core.media.model

import androidx.media3.common.MediaItem
import androidx.media3.common.Player

data class PlayerState(
    val mediaItem: MediaItem = MediaItem.EMPTY,
    val isPlaying: Boolean = false,
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val shuffleModeEnabled: Boolean = false
) {
    companion object {
        val Empty = PlayerState()
    }
}
