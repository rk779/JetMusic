package ml.rk585.jetmusic.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ml.rk585.jetmusic.core.media.MusicPlayer
import ml.rk585.jetmusic.ui.common.LocalMusicPlayer
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    internal lateinit var musicPlayer: MusicPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CompositionLocalProvider(
                LocalMusicPlayer provides musicPlayer
            ) {
                JetMusicApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        musicPlayer.initializeController()
    }

    override fun onStop() {
        super.onStop()
        musicPlayer.releaseController()
    }
}
