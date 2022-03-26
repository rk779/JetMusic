package ml.rk585.jetmusic.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    shape: Shape = RoundedCornerShape(8.dp),
    tonalElevation: Dp = 1.dp,
    shadowElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val containerColor by animateColorAsState(targetValue = backgroundColor)

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border
    ) {
        CompositionLocalProvider(
            LocalContentAlpha provides containerColor.alpha
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelLarge
            ) {
                Row(
                    modifier = Modifier
                        .defaultMinSize(
                            minHeight = 32.dp
                        )
                        .padding(
                            start = if (leadingIcon == null) HorizontalPadding else 0.dp,
                            end = HorizontalPadding
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        val contentColor = when {
                            isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        Spacer(Modifier.width(LeadingIconStartSpacing))
                        CompositionLocalProvider(
                            LocalContentColor provides contentColor,
                            content = leadingIcon
                        )
                        Spacer(Modifier.width(LeadingIconEndSpacing))
                    }
                    content()
                }
            }
        }
    }
}

/**
 * The content padding used by a chip.
 * Used as start padding when there's leading icon, used as eng padding when there's no
 * trailing icon.
 */
private val HorizontalPadding = 16.dp

/**
 * The size of the spacing before the leading icon when they used inside a chip.
 */
private val LeadingIconStartSpacing = 8.dp

/**
 * The size of the spacing between the leading icon and a text inside a chip.
 */
private val LeadingIconEndSpacing = 8.dp
