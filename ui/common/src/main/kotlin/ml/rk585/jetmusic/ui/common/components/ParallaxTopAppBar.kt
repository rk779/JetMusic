package ml.rk585.jetmusic.ui.common.components

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import ml.rk585.jetmusic.ui.common.extensions.drawForegroundGradientScrim
import ml.rk585.jetmusic.ui.common.extensions.iconButtonBackgroundScrim
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.compose.ui.text.lerp as textLerp

/**
 * Top app bar with a background image and a parallax effect.
 *
 * @param title the title to be displayed in the top app bar
 * @param parallaxImagePainter the image painter to be drawn as the top app bar background
 * @param minHeight the minimum height this top app bar is allowed to collapse to
 * @param maxHeight the maximum height this top app bar is allowed to expand to
 * @param modifier the [Modifier] to be applied to this top app bar
 * @param titleEasing an [Easing] function that defines how the title move on the X and Y axis when
 * the top app bar is collapsed or expanded
 * @param navigationIcon the navigation icon displayed at the start of the top app bar. This should
 * typically be an [IconButton] or [IconToggleButton].
 * @param actions the actions displayed at the end of the top app bar. This should typically be
 * [IconButton]s. The default layout here is a [Row], so icons inside will be placed horizontally.
 * @param colors [TopAppBarColors] that will be used to resolve the colors used for this top app
 * bar in different states. See [TopAppBarDefaults.smallTopAppBarColors].
 * @param scrollBehavior a [TopAppBarScrollBehavior] which holds various offset values that will be
 * applied by this top app bar to set up its height and colors. A scroll behavior is designed to
 * work in conjunction with a scrolled content to change the top app bar appearance as the content
 * scrolls. See [TopAppBarScrollBehavior.nestedScrollConnection].
 */
@Composable
fun ParallaxTopAppBar(
    title: @Composable () -> Unit,
    parallaxImagePainter: Painter,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    minHeight: Dp = ParallaxTopAppBarMinHeight,
    maxHeight: Dp = ParallaxTopAppBarMaxHeight,
    titleEasing: Easing = LinearEasing,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val offsetLimit = with(LocalDensity.current) { -maxHeight.toPx() + minHeight.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.offsetLimit != offsetLimit) {
            scrollBehavior?.state?.offsetLimit = offsetLimit
        }
    }

    val scrollFraction = scrollBehavior?.scrollFraction ?: 0f

    // We obtain the parallax scroll fraction via the parallaxScrollFraction() extension function.
    // This fraction computation is always the same, regardless of the behavior that was passed in.
    // We also coerce the scroll fraction to be slightly larger than zero to prevent any
    // titleTextStyle sizing issues when expanding the top app bar all the way down due to float
    // precision.
    val parallaxScrollFraction =
        (scrollBehavior?.parallaxScrollFraction()?.coerceIn(0.000001f, 1f) ?: 0f)

    // Obtain the container color from the TopAppBarColors.
    // This may potentially animate or interpolate a transition between the container-color and the
    // container's scrolled-color according to the app bar's scroll state.
    val appBarContainerColor by colors.containerColor(scrollFraction)

    // Wrap the given actions in a Row.
    val actionsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }

    // Compose a Surface with a TopAppBarLayout content. The surface's background color will be
    // animated as specified above, and the height of the app bar will be determined by the current
    // scroll-state offset.
    Surface(modifier = modifier, color = appBarContainerColor) {
        val height = LocalDensity.current.run {
            maxHeight.toPx() + (scrollBehavior?.state?.offset ?: 0f)
        }
        val heightDp = LocalDensity.current.run {
            height.toDp() + contentPadding.calculateTopPadding()
        }
        val minHeightPx = LocalDensity.current.run { minHeight.roundToPx() }

        Box {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightDp)
            ) {
                Image(
                    painter = parallaxImagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = 1f - parallaxScrollFraction
                            translationY = -parallaxScrollFraction * 0.05f
                        }
                        .drawForegroundGradientScrim(
                            colors
                                .containerColor(scrollFraction = scrollFraction)
                                .value.copy(0.7f)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            ParallaxAppBarLayout(
                modifier = Modifier.padding(contentPadding),
                layoutHeight = height.roundToInt(),
                minHeight = minHeightPx,
                titleEasingFraction = titleEasing.transform(parallaxScrollFraction),
                navigationIconContentColor = colors.navigationIconContentColor(scrollFraction).value,
                titleContentColor = colors.titleContentColor(scrollFraction).value,
                actionIconContentColor = colors.actionIconContentColor(scrollFraction).value,
                title = title,
                titleTextStyle = textLerp(
                    start = MaterialTheme.typography.headlineMedium,
                    stop = MaterialTheme.typography.titleLarge,
                    fraction = parallaxScrollFraction
                ),
                navigationIcon = navigationIcon,
                actions = actionsRow,
            )
        }
    }
}

/**
 * Returns a parallax scroll fraction value. The returned value is computed in a similar way for all
 * any [TopAppBarScrollBehavior].
 */
private fun TopAppBarScrollBehavior.parallaxScrollFraction() =
    if (state.offsetLimit != 0f) state.offset / state.offsetLimit else 0f

@Composable
private fun ParallaxAppBarLayout(
    modifier: Modifier,
    layoutHeight: Int,
    minHeight: Int,
    titleEasingFraction: Float,
    navigationIconContentColor: Color,
    titleContentColor: Color,
    actionIconContentColor: Color,
    title: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    val showIconScrim by derivedStateOf { titleEasingFraction != 1f }

    Layout(
        {
            Box(
                Modifier
                    .layoutId("navigationIcon")
                    .padding(start = TopAppBarHorizontalPadding)
                    .iconButtonBackgroundScrim(showIconScrim)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides navigationIconContentColor,
                    content = navigationIcon
                )
            }
            Box(
                Modifier
                    .layoutId("title")
                    .padding(horizontal = TopAppBarHorizontalPadding)
            ) {
                ProvideTextStyle(value = titleTextStyle) {
                    CompositionLocalProvider(
                        LocalContentColor provides titleContentColor,
                        content = title
                    )
                }
            }
            Box(
                Modifier
                    .layoutId("actionIcons")
                    .padding(end = TopAppBarHorizontalPadding)
                    .iconButtonBackgroundScrim(showIconScrim)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides actionIconContentColor,
                    content = actions
                )
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val navigationIconPlaceable =
            measurables.first { it.layoutId == "navigationIcon" }
                .measure(constraints.copy(minWidth = 0))
        val actionIconsPlaceable =
            measurables.first { it.layoutId == "actionIcons" }
                .measure(constraints.copy(minWidth = 0))

        val maxTitleWidth = if (constraints.maxWidth == Constraints.Infinity) {
            constraints.maxWidth
        } else {
            (constraints.maxWidth - navigationIconPlaceable.width - actionIconsPlaceable.width)
                .coerceAtLeast(0)
        }
        val titlePlaceable =
            measurables.first { it.layoutId == "title" }
                .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))

        // Locate the title's baseline.
        val titleBaseline =
            if (titlePlaceable[LastBaseline] != AlignmentLine.Unspecified) {
                titlePlaceable[LastBaseline]
            } else {
                0
            }

        val titleMinX = TopAppBarTitleInset.roundToPx()
        val titleMaxX = max(TopAppBarTitleInset.roundToPx(), navigationIconPlaceable.width)
        val titleMinY = (layoutHeight - titlePlaceable.height) / 2
        val titleMaxY = layoutHeight - titlePlaceable.height - max(
            TopAppBarTitleBottomPadding.toPx().toInt(),
            TopAppBarTitleBottomPadding.toPx().toInt() - titlePlaceable.height + titleBaseline
        )

        layout(constraints.maxWidth, layoutHeight) {
            // Navigation icon
            navigationIconPlaceable.placeRelative(
                x = 0,
                y = (minHeight - navigationIconPlaceable.height) / 2
            )

            // Title
            titlePlaceable.placeRelative(
                x = lerp(
                    start = titleMinX,
                    stop = titleMaxX,
                    fraction = titleEasingFraction
                ),
                y = lerp(
                    start = titleMaxY,
                    stop = titleMinY,
                    fraction = titleEasingFraction
                )
            )

            // Action icons
            actionIconsPlaceable.placeRelative(
                x = constraints.maxWidth - actionIconsPlaceable.width,
                y = (minHeight - actionIconsPlaceable.height) / 2
            )
        }
    }
}

private val ParallaxTopAppBarMinHeight = 64.dp
private val ParallaxTopAppBarMaxHeight = 180.dp
private val TopAppBarHorizontalPadding = 4.dp
private val TopAppBarTitleBottomPadding = 4.dp

// A title inset when the App-Bar is a Medium or Large one. Also used to size a spacer when the
// navigation icon is missing.
private val TopAppBarTitleInset = 16.dp - TopAppBarHorizontalPadding
