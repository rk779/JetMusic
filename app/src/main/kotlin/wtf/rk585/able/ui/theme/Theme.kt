package wtf.rk585.able.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Green200,
    primaryVariant = Green200,
    secondary = Green200,
    background = Jaguar,
    surface = Jaguar
)

private val LightColorPalette = lightColors(
    primary = Green400,
    primaryVariant = Green400,
    secondary = Green400,
    secondaryVariant = Green400,
    onPrimary = Color.Black
)

@Composable
fun AbleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        shapes = shapes,
        content = content
    )
}