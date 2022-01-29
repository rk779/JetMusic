package ml.rk585.jetmusic.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val darkColorScheme = darkColorScheme()
val lightColorScheme = lightColorScheme()

fun ColorScheme.toMaterial2Colors(isLight: Boolean): Colors {
    return Colors(
        primary = primary,
        onPrimary = onPrimary,
        primaryVariant = primary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryVariant = secondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError,
        isLight = isLight
    )
}
