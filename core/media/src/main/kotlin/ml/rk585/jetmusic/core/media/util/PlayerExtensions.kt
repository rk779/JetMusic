package ml.rk585.jetmusic.core.media.util

import androidx.media3.common.Player
import logcat.logcat

fun Player.playPause() {
    when {
        isPlaying -> pause()
        !isPlaying -> play()
        else -> logcat { "Couldn't play or pause Player." }
    }
}

fun Player.toggleRepeatMode() {
    repeatMode = when (repeatMode) {
        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
        Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
        Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
        else -> Player.REPEAT_MODE_OFF
    }
}

fun Player.toggleShuffleMode() {
    shuffleModeEnabled = !shuffleModeEnabled
}
