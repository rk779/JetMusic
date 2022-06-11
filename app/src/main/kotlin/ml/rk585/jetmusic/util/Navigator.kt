package ml.rk585.jetmusic.util

import androidx.navigation.NavController
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ml.rk585.jetmusic.ui.artist.ArtistNavigator
import ml.rk585.jetmusic.ui.artist.destinations.ArtistDestination
import ml.rk585.jetmusic.ui.home.HomeNavigator
import ml.rk585.jetmusic.ui.player.PlayerNavigator
import ml.rk585.jetmusic.ui.playlist.PlaylistNavigator
import ml.rk585.jetmusic.ui.playlist.destinations.PlaylistDestination
import ml.rk585.jetmusic.ui.search.SearchNavigator
import ml.rk585.jetmusic.ui.settings.SettingsNavigator
import ml.rk585.jetmusic.ui.settings.destinations.SettingsDestination

internal class Navigator(
    private val navController: NavController,
    private val navGraphSpec: NavGraphSpec
) : ArtistNavigator,
    HomeNavigator,
    PlayerNavigator,
    PlaylistNavigator,
    SearchNavigator,
    SettingsNavigator {

    override fun openSettings() {
        navController.navigate(SettingsDestination within navGraphSpec)
    }

    override fun onNavigateUp() {
        navController.navigateUp()
    }

    override fun openArtist(id: String) {
        navController.navigate(ArtistDestination(id) within navGraphSpec)
    }

    override fun openPlaylist(id: String) {
        navController.navigate(PlaylistDestination(id) within navGraphSpec)
    }
}
