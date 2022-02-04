package ml.rk585.jetmusic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import ml.rk585.jetmusic.ui.screens.home.HomeScreen
import ml.rk585.jetmusic.ui.screens.player.PlayerSheet
import ml.rk585.jetmusic.ui.screens.playlist.PlaylistScreen

private sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Player : Screen("player")

    object Playlist : Screen("playlist/{playlistUrl}") {
        fun createRoute(playlistUrl: String): String {
            return "playlist/$playlistUrl"
        }
    }
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {
        addSearch(navController)
        addPlayer(navController)
        addPlaylist(navController)
    }
}

private fun NavGraphBuilder.addSearch(navController: NavController) {
    composable(
        route = Screen.Search.route
    ) {
        HomeScreen(
            openPlayer = {
                navController.navigate(Screen.Player.route)
            },
            openPlaylistDetail = { playlistUri ->
                navController.navigate(Screen.Playlist.createRoute(playlistUri))
            }
        )
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.addPlayer(navController: NavController) {
    bottomSheet(
        route = Screen.Player.route
    ) {
        PlayerSheet(
            onClose = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.addPlaylist(navController: NavController) {
    composable(
        route = Screen.Playlist.route
    ) {
        PlaylistScreen(
            onNavigateUp = navController::navigateUp
        )
    }
}
