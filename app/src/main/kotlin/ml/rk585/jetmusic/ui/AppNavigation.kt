package ml.rk585.jetmusic.ui

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.manualcomposablecalls.DestinationScope
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import ml.rk585.jetmusic.ui.artist.destinations.ArtistDestination
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.home.destinations.HomeDestination
import ml.rk585.jetmusic.ui.library.destinations.LibraryDestination
import ml.rk585.jetmusic.ui.player.destinations.PlayerDestination
import ml.rk585.jetmusic.ui.playlist.destinations.PlaylistDestination
import ml.rk585.jetmusic.ui.search.destinations.SearchDestination
import ml.rk585.jetmusic.ui.settings.destinations.SettingsDestination
import ml.rk585.jetmusic.util.Navigator

internal object NavGraphs {

    val home = object : NavGraphSpec {
        override val route: String = "home"
        override val startRoute: Route = HomeDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            HomeDestination,
            SettingsDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val library = object : NavGraphSpec {
        override val route: String = "library"
        override val startRoute: Route = LibraryDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            LibraryDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val player = object : NavGraphSpec {
        override val route: String = "player"
        override val startRoute: Route = PlayerDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            PlayerDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val search = object : NavGraphSpec {
        override val route: String = "search"
        override val startRoute: Route = SearchDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SearchDestination,
            ArtistDestination,
            PlaylistDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = home
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(
            home,
            search,
            library,
            player
        )
    }
}

internal fun DestinationScope<*>.currentNavigator(): Navigator {
    return Navigator(
        navController,
        navBackStackEntry.destination.navGraph()
    )
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navHostEngine = rememberAnimatedNavHostEngine()

    DestinationsNavHost(
        navController = navController,
        navGraph = NavGraphs.root,
        engine = navHostEngine,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
        }
    )
}

internal enum class BottomBarDestinations(
    val navGraphSpec: NavGraphSpec,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val unselectedIcon: ImageVector = icon
) {
    HOME(
        navGraphSpec = NavGraphs.home,
        labelRes = R.string.home,
        icon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    SEARCH(
        navGraphSpec = NavGraphs.search,
        labelRes = R.string.search,
        icon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),
    LIBRARY(
        navGraphSpec = NavGraphs.library,
        labelRes = R.string.your_library,
        icon = Icons.Filled.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic
    )
}
