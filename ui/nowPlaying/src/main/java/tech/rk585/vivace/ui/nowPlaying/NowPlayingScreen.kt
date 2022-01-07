package tech.rk585.vivace.ui.nowPlaying

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NowPlayingScreen() {

}

@Composable
fun ProvidePlayerViewModel(content: @Composable () -> Unit) {
    val playerViewModel: PlayerViewModel = viewModel()

    CompositionLocalProvider(
        LocalPlayerViewModel provides playerViewModel,
        content = content
    )
}

val LocalPlayerViewModel =
    staticCompositionLocalOf<PlayerViewModel> { error("PlayerViewModel not provided") }