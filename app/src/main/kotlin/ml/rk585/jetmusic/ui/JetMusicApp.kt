package ml.rk585.jetmusic.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import ml.rk585.jetmusic.ui.components.ModalBottomSheetLayout
import ml.rk585.jetmusic.ui.components.rememberBottomSheetNavigator
import ml.rk585.jetmusic.ui.screens.NavGraphs
import ml.rk585.jetmusic.ui.common.theme.JetMusicTheme

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun JetMusicApp() {
    JetMusicTheme {
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        val navController = rememberAnimatedNavController(bottomSheetNavigator)
        val navHostEngine = rememberAnimatedNavHostEngine()

        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            scrimColor = Color.Transparent
        ) {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                engine = navHostEngine,
                navController = navController
            )
        }
    }
}
