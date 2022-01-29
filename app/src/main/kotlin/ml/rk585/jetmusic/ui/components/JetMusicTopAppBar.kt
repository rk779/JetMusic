package ml.rk585.jetmusic.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Composable
fun JetMusicTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.statusBars,
        applyBottom = false
    ),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    appBarStyle: AppBarStyle = AppBarStyle.Medium,
    colors: TopAppBarColors = TopAppBarDefaults.fromAppBarStyle(appBarStyle),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    val containerColor by colors.containerColor(scrollFraction)

    Surface(
        modifier = modifier,
        color = containerColor
    ) {
        when (appBarStyle) {
            AppBarStyle.Small -> {
                SmallTopAppBar(
                    title = title,
                    modifier = Modifier.padding(contentPadding),
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
            AppBarStyle.Medium -> {
                MediumTopAppBar(
                    title = title,
                    modifier = Modifier.padding(contentPadding),
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
            AppBarStyle.Large -> {
                LargeTopAppBar(
                    title = title,
                    modifier = Modifier.padding(contentPadding),
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
        }
    }
}

enum class AppBarStyle {
    Small, Medium, Large
}

@Composable
internal fun TopAppBarDefaults.fromAppBarStyle(style: AppBarStyle): TopAppBarColors {
    return when (style) {
        AppBarStyle.Small -> smallTopAppBarColors()
        AppBarStyle.Medium -> mediumTopAppBarColors()
        AppBarStyle.Large -> largeTopAppBarColors()
    }
}
