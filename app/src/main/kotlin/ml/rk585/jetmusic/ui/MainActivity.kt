package ml.rk585.jetmusic.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import ml.rk585.jetmusic.ui.screens.home.HomeScreen
import ml.rk585.jetmusic.ui.theme.JetMusicTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            JetMusicTheme {
                ProvideWindowInsets {
                    HomeScreen()
                }
            }
        }
    }
}
