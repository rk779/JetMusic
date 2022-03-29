package ml.rk585.jetmusic.ui.common

import androidx.compose.runtime.compositionLocalOf
import ml.rk585.jetmusic.core.media.MusicPlayer

val LocalMusicPlayer = compositionLocalOf<MusicPlayer> { error("MusicPlayer not provided") }
