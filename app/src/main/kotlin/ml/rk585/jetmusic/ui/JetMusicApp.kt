package ml.rk585.jetmusic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import ml.rk585.jetmusic.ui.components.ModalBottomSheetLayout
import ml.rk585.jetmusic.ui.components.rememberBottomSheetNavigator
import ml.rk585.jetmusic.ui.theme.JetMusicTheme

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun JetMusicApp() {
    JetMusicTheme {
        ProvideWindowInsets {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberNavController(bottomSheetNavigator)

            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                scrimColor = Color.Transparent
            ) {
                AppNavigation(navController)
            }
        }
    }
}
