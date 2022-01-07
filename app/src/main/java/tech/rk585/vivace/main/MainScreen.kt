package tech.rk585.vivace.main

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import tech.rk585.vivace.ui.common.compose.components.BottomNavigationBar
import tech.rk585.vivace.ui.common.compose.components.BottomNavigationItem
import tech.rk585.vivace.ui.common.compose.theme.translucentSurfaceColor
import tech.rk585.vivace.ui.nowPlaying.LocalPlayerViewModel
import tech.rk585.vivace.ui.nowPlaying.components.PlayerBottomBar
import tech.rk585.vivace.ui.nowPlaying.components.PlayerScreen

@Composable
internal fun MainScreen(
    backPressedDispatcher: OnBackPressedDispatcher
) {

    val navController = rememberNavController()
    val navigateToScreen: (Screen) -> Unit = { screen ->
        navController.navigate(screen.route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }
    val selectedScreen by navController.currentScreenAsState()
    val playerViewModel = LocalPlayerViewModel.current

    Scaffold(
        bottomBar = {
            Column {
                if (!playerViewModel.showFullScreen) {
                    PlayerBottomBar(
                        modifier = Modifier.fillMaxWidth()
                    )
                    BottomNavigationBar(
                        selectedScreen = selectedScreen,
                        navigateToScreen = navigateToScreen
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.fillMaxSize()) {
            AppNavigation(navController)
            PlayerScreen(
                backPressedDispatcher = backPressedDispatcher
            )
        }
    }
}


/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the return [State]
 * as the destination changes.
 */
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedScreen = remember { mutableStateOf(Screen.Home) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Home.route } -> {
                    selectedScreen.value = Screen.Home
                }
                destination.hierarchy.any { it.route == Screen.Search.route } -> {
                    selectedScreen.value = Screen.Search
                }
                destination.hierarchy.any { it.route == Screen.Library.route } -> {
                    selectedScreen.value = Screen.Library
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedScreen
}

@Composable
private fun BottomNavigationBar(
    selectedScreen: Screen,
    navigateToScreen: (Screen) -> Unit
) {
    BottomNavigationBar(
        backgroundColor = translucentSurfaceColor(),
        elevation = 4.dp
    ) {
        Screen.values().forEach { screen ->
            BottomNavigationItem(
                selected = selectedScreen == screen,
                selectedVector = screen.selectedIcon,
                vector = screen.icon,
                contentDescription = stringResource(id = screen.labelRes),
                label = stringResource(id = screen.labelRes),
                onClick = {
                    if (screen != selectedScreen) {
                        navigateToScreen(screen)
                    }
                }
            )
        }
    }
}