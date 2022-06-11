package ml.rk585.jetmusic.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ml.rk585.jetmusic.ui.common.components.JetMusicBottomNavigationBar
import ml.rk585.jetmusic.ui.common.components.ModalBottomSheetLayout
import ml.rk585.jetmusic.ui.common.components.rememberBottomSheetNavigator
import ml.rk585.jetmusic.ui.common.theme.JetMusicTheme
import ml.rk585.jetmusic.ui.player.miniPlayer.MiniPlayer

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun JetMusicApp() {
    JetMusicTheme {
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        val navController = rememberAnimatedNavController(bottomSheetNavigator)

        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            scrimColor = Color.Transparent
        ) {
            JetMusic(
                navController = navController
            )
        }
    }
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun JetMusic(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val currentNavGraphSpec by navController.currentNavGraphSpecAsState()
    val navigateToNavGraphSpec: (NavGraphSpec) -> Unit = { navGraphSpec ->
        navController.navigate(navGraphSpec) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                MiniPlayer(
                    openPlayerSheet = { navController.navigate(NavGraphs.player) },
                    modifier = Modifier.fillMaxWidth()
                )
                BottomNavigationBar(
                    selectedNavGraphSpec = currentNavGraphSpec,
                    onNavGraphSpecSelected = navigateToNavGraphSpec,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
internal fun BottomNavigationBar(
    selectedNavGraphSpec: NavGraphSpec,
    onNavGraphSpecSelected: (NavGraphSpec) -> Unit,
    modifier: Modifier = Modifier,
) {
    JetMusicBottomNavigationBar(modifier) {
        BottomBarDestinations.values().forEach { item ->
            NavigationBarItem(
                selected = selectedNavGraphSpec == item.navGraphSpec,
                onClick = { onNavGraphSpecSelected(item.navGraphSpec) },
                icon = {
                    Crossfade(targetState = selectedNavGraphSpec == item.navGraphSpec) { state ->
                        Icon(
                            imageVector = if (state) item.icon else item.unselectedIcon,
                            contentDescription = stringResource(id = item.labelRes)
                        )
                    }
                },
                label = { Text(text = stringResource(id = item.labelRes)) }
            )
        }
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
internal fun NavController.currentNavGraphSpecAsState(): State<NavGraphSpec> {
    val selectedItem = remember { mutableStateOf(NavGraphs.home) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.navGraph()
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

internal fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    throw RuntimeException("Unknown nav graph for destination $route")
}
