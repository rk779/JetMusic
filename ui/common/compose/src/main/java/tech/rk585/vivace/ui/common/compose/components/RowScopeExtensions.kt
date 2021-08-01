package tech.rk585.vivace.ui.common.compose.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RowScope.BottomNavigationItem(
    selected: Boolean,
    selectedVector: ImageVector,
    vector: ImageVector,
    contentDescription: String,
    label: String,
    onClick: () -> Unit
) {
    BottomNavigationItem(
        icon = {
            Crossfade(targetState = selected) { selected ->
                Icon(
                    imageVector = if (selected) selectedVector else vector,
                    contentDescription = contentDescription
                )
            }
        },
        label = {
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        selected = selected,
        alwaysShowLabel = false,
        onClick = onClick
    )
}