package wtf.rk585.able.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import wtf.rk585.able.R

/**
 * Destinations for navigation within the app.
 */
enum class Destination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    Home("home", R.string.nav_home, Rounded.Home),
    Search("search", R.string.nav_search, Rounded.Search),
    Library("library", R.string.nav_library, Rounded.LibraryMusic);

    companion object {
        val startDestination = Home
    }
}