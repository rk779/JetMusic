package ml.rk585.jetmusic.util

import androidx.navigation.NavController
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ml.rk585.jetmusic.ui.home.HomeNavigator
import ml.rk585.jetmusic.ui.settings.SettingsNavigator
import ml.rk585.jetmusic.ui.settings.destinations.SettingsDestination

internal class Navigator(
    private val navController: NavController,
    private val navGraphSpec: NavGraphSpec
) : HomeNavigator,
    SettingsNavigator {

    override fun openSettings() {
        navController.navigateTo(SettingsDestination within navGraphSpec)
    }

    override fun onNavigateUp() {
        navController.navigateUp()
    }
}
