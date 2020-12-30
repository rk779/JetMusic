package wtf.rk585.able

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import wtf.rk585.able.ui.navigation.Destination
import wtf.rk585.able.ui.theme.AbleTheme
import wtf.rk585.able.ui.views.HomeScreen
import wtf.rk585.able.ui.views.LibraryScreen
import wtf.rk585.able.ui.views.SearchScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AbleTheme {
                AbleApp()
            }
        }
    }
}

@Composable
fun AbleApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AbleBottomNav(navHostController = navController)
        }
    ) {
        NavHost(navController, startDestination = Destination.startDestination.route) {
            composable(Destination.Home.route) {
                HomeScreen()
            }
            composable(Destination.Search.route) {
                SearchScreen()
            }
            composable(Destination.Library.route) {
                LibraryScreen()
            }
        }
    }
}

@Composable
fun AbleBottomNav(
    navHostController: NavHostController
) {
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.colorBackground)
    ) {
        val navStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute =
            navStackEntry?.arguments?.getString(KEY_ROUTE) ?: Destination.startDestination.route

        Destination.values().forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon) },
                label = { Text(stringResource(id = screen.labelRes)) },
                selected = currentRoute == screen.route,
                alwaysShowLabels = false,
                onClick = {
                    if (currentRoute == screen.route) return@BottomNavigationItem
                    navHostController.popBackStack(navHostController.graph.startDestination, false)
                    if (screen.route != Destination.startDestination.route) {
                        navHostController.navigate(screen.route)
                    }
                }
            )
        }
    }
}