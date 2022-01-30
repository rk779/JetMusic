package ml.rk585.jetmusic.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.m3Colors()
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        colors = colors
    )
}

@Composable
fun SliderDefaults.m3Colors(
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    disabledThumbColor: Color = MaterialTheme.colorScheme.onSurface
        .copy(alpha = 0.38f)
        .compositeOver(MaterialTheme.colorScheme.surface),
    activeTrackColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledActiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    disabledInactiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    activeTickColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
    inactiveTickColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
    disabledActiveTickColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    disabledInactiveTickColor: Color = disabledActiveTickColor
): SliderColors {
    return colors(
        thumbColor = thumbColor,
        disabledThumbColor = disabledThumbColor,
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
        activeTickColor = activeTickColor,
        inactiveTickColor = inactiveTickColor,
        disabledActiveTickColor = disabledActiveTickColor,
        disabledInactiveTickColor = disabledInactiveTickColor
    )
}
