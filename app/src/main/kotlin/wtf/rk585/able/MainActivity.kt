package wtf.rk585.able

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import wtf.rk585.able.ui.navigation.Destination
import wtf.rk585.able.ui.theme.AbleTheme
import wtf.rk585.able.util.IconResource

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
    ) { innerPadding ->
        NavHost(navController, startDestination = Destination.startDestination.route) {
            composable(Destination.Home.route) {

            }
            composable(Destination.Search.route) {

            }
            composable(Destination.Library.route) {

            }
        }
    }
}

@Composable
fun AbleBottomNav(
    navHostController: NavHostController
) {
    BottomNavigation {
        val navStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute =
            navStackEntry?.arguments?.getString(KEY_ROUTE) ?: Destination.startDestination.route

        Destination.values().forEach { screen ->
            BottomNavigationItem(
                icon = { IconResource(resourceId = screen.drawableRes) },
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