package ml.rk585.jetmusic.ui.common.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ModalBottomSheetLayout(
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    sheetShape: Shape = RectangleShape,
    sheetTonalElevation: Dp = 1.dp,
    sheetContainerColor: Color = MaterialTheme.colorScheme.surface,
    sheetContentColor: Color = contentColorFor(sheetContainerColor),
    scrimColor: Color = MaterialTheme.colorScheme.inverseSurface.copy(0.4f),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides sheetContentColor,
    ) {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            modifier = modifier,
            sheetShape = sheetShape,
            sheetElevation = sheetTonalElevation,
            sheetBackgroundColor = sheetContainerColor,
            sheetContentColor = sheetContentColor,
            scrimColor = scrimColor,
            content = content
        )
    }
}
