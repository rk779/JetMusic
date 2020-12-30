package wtf.rk585.able.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import wtf.rk585.able.R

/**
 * Destinations for navigation within the app.
 */
enum class Destination(
    val route: String,
    @StringRes val labelRes: Int,
    @DrawableRes val drawableRes: Int
) {
    Home("home", R.string.home, R.drawable.ic_home_24dp),
    Search("search", R.string.search, R.drawable.ic_search_24dp),
    Library("library", R.string.library, R.drawable.ic_library_music_24dp);

    companion object {
        val startDestination = Home
    }
}