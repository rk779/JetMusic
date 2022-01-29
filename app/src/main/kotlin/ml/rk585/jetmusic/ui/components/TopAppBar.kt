package ml.rk585.jetmusic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
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
fun SmallTopAppBar(
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
    scrollBehavior: TopAppBarScrollBehavior? = null,
    bottomContent: @Composable ColumnScope.() -> Unit = {}
) {
    val scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    val containerColor by colors.containerColor(scrollFraction)

    Surface(
        modifier = modifier,
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            SmallTopAppBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                colors = colors,
                scrollBehavior = scrollBehavior
            )

            bottomContent()
        }
    }
}
