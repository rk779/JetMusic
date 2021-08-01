package tech.rk585.vivace.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.rk585.vivace.ui.common.compose.R
import tech.rk585.vivace.ui.home.HomeScreen
import tech.rk585.vivace.ui.library.LibraryScreen
import tech.rk585.vivace.ui.nowPlaying.NowPlayingScreen
import tech.rk585.vivace.ui.search.SearchScreen

internal enum class Screen(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    Home(
        route = "homeRoot",
        labelRes = R.string.nav_home,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    Search(
        route = "searchRoot",
        labelRes = R.string.nav_search,
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search
    ),
    Library(
        route = "libraryRoot",
        labelRes = R.string.nav_library,
        icon = Icons.Outlined.LibraryMusic,
        selectedIcon = Icons.Filled.LibraryMusic
    )
}

private sealed class LeafScreen(
    val route: String
) {
    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    object Library : LeafScreen("library")
    object NowPlaying : LeafScreen("nowPlaying")
}

@Composable
internal fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        addHomeScreenTopLevel()
        addSearchScreenTopLevel()
        addLibraryScreenTopLevel()
    }
}

private fun NavGraphBuilder.addHomeScreenTopLevel() {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        addHomeScreen()
    }
}

private fun NavGraphBuilder.addSearchScreenTopLevel() {
    navigation(
        route = Screen.Search.route,
        startDestination = LeafScreen.Search.route
    ) {
        addSearchScreen()
    }
}

private fun NavGraphBuilder.addLibraryScreenTopLevel() {
    navigation(
        route = Screen.Library.route,
        startDestination = LeafScreen.Library.route
    ) {
        addLibraryScreen()
    }
}

private fun NavGraphBuilder.addHomeScreen() {
    composable(
        route = LeafScreen.Home.route
    ) {
        HomeScreen()
    }
}

private fun NavGraphBuilder.addSearchScreen() {
    composable(
        route = LeafScreen.Search.route
    ) {
        SearchScreen()
    }
}

private fun NavGraphBuilder.addLibraryScreen() {
    composable(
        route = LeafScreen.Library.route
    ) {
        LibraryScreen()
    }
}

private fun NavGraphBuilder.addNowPlayingScreen() {
    composable(
        route = LeafScreen.NowPlaying.route
    ) {
        NowPlayingScreen()
    }
}