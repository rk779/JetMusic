/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ml.rk585.jetmusic.ui.common.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Provides the current [Scaffold] content padding values.
 */
val LocalScaffoldPadding: ProvidableCompositionLocal<PaddingValues> =
    staticCompositionLocalOf { PaddingValues(0.dp) }

/**
 * @param modifier optional Modifier for the root of the [Scaffold].
 * @param topBar top app bar of the screen. Consider using [SmallTopAppBar].
 * @param bottomBar bottom bar of the screen. Consider using [NavigationBar].
 * @param snackbarHost component to host [Snackbar]s that are pushed to be shown via
 * [SnackbarHostState.showSnackbar]. Usually it's a [SnackbarHost].
 * @param floatingActionButton Main action button of your screen. Consider using
 * [FloatingActionButton] for this slot.
 * @param floatingActionButtonPosition position of the FAB on the screen. See [FabPosition] for
 * possible options available.
 * @param containerColor background of the scaffold body
 * @param contentColor color of the content in scaffold body. Defaults to either the matching
 * content color for [containerColor], or, if it is not a color from the theme, this will keep
 * the same value set above this Surface.
 * @param contentPadding Additional content padding to apply to the [content].
 * @param content content of your screen. The lambda receives an [PaddingValues] that should be
 * applied to the content root via Modifier.padding to properly offset top and bottom bars. If
 * you're using Modifier.VerticalScroll, apply this modifier to the child of the scroll, and not on
 * the scroll itself.
 */
@ExperimentalMaterial3Api
@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentPadding: PaddingValues = LocalScaffoldPadding.current,
    content: @Composable (PaddingValues) -> Unit
) {
    Surface(modifier = modifier, color = containerColor, contentColor = contentColor) {
        ScaffoldLayout(
            fabPosition = floatingActionButtonPosition,
            topBar = topBar,
            bottomBar = bottomBar,
            content = content,
            snackbar = snackbarHost,
            fab = floatingActionButton,
            contentPadding = contentPadding
        )
    }
}

/**
 * Layout for a [Scaffold]'s content.
 *
 * @param fabPosition [FabPosition] for the FAB (if present)
 * @param topBar the content to place at the top of the [Scaffold], typically a [SmallTopAppBar]
 * @param content the main 'body' of the [Scaffold]
 * @param snackbar the [Snackbar] displayed on top of the [content]
 * @param fab the [FloatingActionButton] displayed on top of the [content], below the [snackbar]
 * and above the [bottomBar]
 * @param bottomBar the content to place at the bottom of the [Scaffold], on top of the
 * [content], typically a [NavigationBar].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldLayout(
    fabPosition: FabPosition,
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    snackbar: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    contentPadding: PaddingValues
) {
    val innerPadding = remember { MutablePaddingValues() }

    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val topBarPlaceables = subcompose(ScaffoldLayoutContent.TopBar, topBar).map {
                it.measure(looseConstraints)
            }

            val topBarHeight = topBarPlaceables.maxByOrNull { it.height }?.height ?: 0

            val snackbarPlaceables = subcompose(ScaffoldLayoutContent.Snackbar, snackbar).map {
                it.measure(looseConstraints)
            }

            val snackbarHeight = snackbarPlaceables.maxByOrNull { it.height }?.height ?: 0
            val snackbarWidth = snackbarPlaceables.maxByOrNull { it.width }?.width ?: 0

            val fabPlaceables =
                subcompose(ScaffoldLayoutContent.Fab, fab).mapNotNull { measurable ->
                    measurable.measure(looseConstraints).takeIf { it.height != 0 && it.width != 0 }
                }

            val fabPlacement = if (fabPlaceables.isNotEmpty()) {
                val fabWidth = fabPlaceables.maxByOrNull { it.width }!!.width
                val fabHeight = fabPlaceables.maxByOrNull { it.height }!!.height
                // FAB distance from the left of the layout, taking into account LTR / RTL
                val fabLeftOffset = if (fabPosition == FabPosition.End) {
                    if (layoutDirection == LayoutDirection.Ltr) {
                        layoutWidth - FabSpacing.roundToPx() - fabWidth
                    } else {
                        FabSpacing.roundToPx()
                    }
                } else {
                    (layoutWidth - fabWidth) / 2
                }

                FabPlacement(
                    left = fabLeftOffset,
                    width = fabWidth,
                    height = fabHeight
                )
            } else {
                null
            }

            val bottomBarPlaceables = subcompose(ScaffoldLayoutContent.BottomBar) {
                CompositionLocalProvider(
                    LocalFabPlacement provides fabPlacement,
                    content = bottomBar
                )
            }.map { it.measure(looseConstraints) }

            val bottomBarHeight = bottomBarPlaceables.maxByOrNull { it.height }?.height ?: 0
            val fabOffsetFromBottom = fabPlacement?.let {
                if (bottomBarHeight == 0) {
                    it.height + FabSpacing.roundToPx() + contentPadding.calculateBottomPadding()
                        .roundToPx()
                } else {
                    // Total height is the bottom bar height + the FAB height + the padding
                    // between the FAB and bottom bar
                    bottomBarHeight + it.height + FabSpacing.roundToPx()
                }
            }

            val snackbarOffsetFromBottom = if (snackbarHeight != 0) {
                snackbarHeight + (fabOffsetFromBottom ?: bottomBarHeight)
            } else {
                0
            }

            // Update the inner padding
            innerPadding.apply {
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr)
                top = topBarHeight.toDp() + contentPadding.calculateTopPadding()
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                bottom = bottomBarHeight.toDp() + contentPadding.calculateBottomPadding()
            }

            val bodyContentPlaceables = subcompose(ScaffoldLayoutContent.MainContent) {
                CompositionLocalProvider(LocalScaffoldPadding provides innerPadding) {
                    content(innerPadding)
                }
            }.map { it.measure(looseConstraints.copy(maxHeight = layoutHeight)) }

            // Placing to control drawing order to match default elevation of each placeable

            bodyContentPlaceables.forEach { it.place(0, 0) }
            topBarPlaceables.forEach { it.place(0, 0) }
            snackbarPlaceables.forEach {
                it.place(
                    (layoutWidth - snackbarWidth) / 2,
                    layoutHeight - snackbarOffsetFromBottom
                )
            }
            // The bottom bar is always at the bottom of the layout
            bottomBarPlaceables.forEach {
                it.place(0, layoutHeight - bottomBarHeight)
            }
            // Explicitly not using placeRelative here as `leftOffset` already accounts for RTL
            fabPlacement?.let { placement ->
                fabPlaceables.forEach {
                    it.place(placement.left, layoutHeight - fabOffsetFromBottom!!)
                }
            }
        }
    }
}

/**
 * Placement information for a [FloatingActionButton] inside a [Scaffold].
 *
 * @property left the FAB's offset from the left edge of the bottom bar, already adjusted for RTL
 * support
 * @property width the width of the FAB
 * @property height the height of the FAB
 */
@Immutable
internal class FabPlacement(
    val left: Int,
    val width: Int,
    val height: Int
)

/**
 * CompositionLocal containing a [FabPlacement] that is used to calculate the FAB bottom offset.
 */
internal val LocalFabPlacement = staticCompositionLocalOf<FabPlacement?> { null }

// FAB spacing above the bottom bar / bottom of the Scaffold
private val FabSpacing = 16.dp

private enum class ScaffoldLayoutContent { TopBar, MainContent, Snackbar, Fab, BottomBar }

@Stable
internal class MutablePaddingValues : PaddingValues {
    var start: Dp by mutableStateOf(0.dp)
    var top: Dp by mutableStateOf(0.dp)
    var end: Dp by mutableStateOf(0.dp)
    var bottom: Dp by mutableStateOf(0.dp)

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return when (layoutDirection) {
            LayoutDirection.Ltr -> start
            LayoutDirection.Rtl -> end
        }
    }

    override fun calculateTopPadding(): Dp = top

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return when (layoutDirection) {
            LayoutDirection.Ltr -> end
            LayoutDirection.Rtl -> start
        }
    }

    override fun calculateBottomPadding(): Dp = bottom
}
