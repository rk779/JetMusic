package tech.rk585.vivace.ui.common.compose.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import androidx.core.math.MathUtils
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.rk585.vivace.ui.common.compose.theme.contrastComposite
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import android.graphics.Color as AColor

val ADAPTIVE_COLOR_ANIMATION: AnimationSpec<Color> = tween(easing = FastOutSlowInEasing)

data class AdaptiveColorResult(val color: Color, val contentColor: Color, val gradient: Brush)

val LocalAdaptiveColorResult = staticCompositionLocalOf<AdaptiveColorResult> {
    error("No LocalAdaptiveColorResult provided")
}

private val adaptiveColorCache = mutableMapOf<String, Color>()

@Composable
fun adaptiveColor(
    imageData: Any?,
    fallback: Color = MaterialTheme.colors.secondary.contrastComposite(),
    initial: Color = fallback,
    animationSpec: AnimationSpec<Color> = ADAPTIVE_COLOR_ANIMATION,
    gradientEndColor: Color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
): State<AdaptiveColorResult> {
    val context = LocalContext.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    if (imageData != null)
        LaunchedEffect(imageData) {
            launch(Dispatchers.Unconfined) {
                val request = ImageRequest.Builder(context)
                    .data(imageData)
                    .size(300)
                    .precision(Precision.INEXACT)
                    .allowHardware(false)
                    .build()

                when (val result = context.imageLoader.execute(request)) {
                    is SuccessResult -> {
                        bitmap = (result.drawable as BitmapDrawable).bitmap
                    }
                    is ErrorResult -> {
                        Log.e(context.applicationInfo.name, "adaptiveColor: ", result.throwable)
                    }
                }
            }
        }

    return adaptiveColor(
        image = bitmap,
        imageSource = imageData,
        fallback = fallback,
        initial = initial,
        animationSpec = animationSpec,
        gradientEndColor = gradientEndColor
    )
}

@Composable
fun adaptiveColor(
    image: Bitmap? = null,
    imageSource: Any? = image,
    fallback: Color = MaterialTheme.colors.secondary.contrastComposite(),
    initial: Color = fallback,
    animationSpec: AnimationSpec<Color> = ADAPTIVE_COLOR_ANIMATION,
    gradientEndColor: Color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
    isDarkColors: Boolean = !MaterialTheme.colors.isLight
): State<AdaptiveColorResult> {
    val imageHash = imageSource.hashCode().toString()
    val initialAccent = adaptiveColorCache.getOrElse(imageHash) { initial }

    var accent by remember { mutableStateOf(initialAccent) }
    val accentAnimated by animateColorAsState(accent, animationSpec)
    val contentColor by derivedStateOf { accent.contentColor() }

    var paletteGenerated by remember { mutableStateOf(false) }
    var delayInitialFallback by remember { mutableStateOf(imageSource != null) }

    LaunchedEffect(image, fallback, isDarkColors) {
        if (image != null && imageSource != null) {
            accent = adaptiveColorCache.getOrPut(imageHash) {
                val palette = Palette.from(image).generate()
                getAccentColor(isDarkColors, fallback.toArgb(), palette).run { Color(this) }
            }
            paletteGenerated = true
        }
    }

    // when fallback color changes
    // reset initial accent color if palette hasn't been generated yet
    LaunchedEffect(fallback) {
        if (delayInitialFallback) delay(1000)
        if (!paletteGenerated) {
            accent = fallback
            delayInitialFallback = false
        }
    }

    return derivedStateOf {
        AdaptiveColorResult(
            accentAnimated,
            contentColor,
            backgroundGradient(accentAnimated, gradientEndColor, isDarkColors)
        )
    }
}

fun backgroundGradient(
    accent: Color,
    endColor: Color,
    isDark: Boolean,
): Brush {
    val first = gradientShift(isDark, accent.toArgb(), 0.4f, 100)
    val second = gradientShift(isDark, accent.toArgb(), 0.26f, 66)
    val third = gradientShift(isDark, accent.toArgb(), 0.13f, 33)

    return Brush.verticalGradient(listOf(first, second, third, endColor))
}

/**
 * Applies linear gradient background with given [colorStops] and [angle].
 */
fun Modifier.gradientBackground(
    vararg colorStops: Pair<Float, Color>,
    angle: Float
): Modifier {
    return drawBehind {
        val angleRad = angle / 180f * PI
        val x = cos(angleRad).toFloat() // Fractional x
        val y = sin(angleRad).toFloat() // Fractional y

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colorStops = colorStops,
                start = Offset(size.width, size.height) - exactOffset,
                end = exactOffset
            ),
            size = size
        )
    }
}

fun getAccentColor(isDark: Boolean, default: Int, palette: Palette): Int {
    when (isDark) {
        true -> {
            val darkMutedColor = palette.getDarkMutedColor(default)
            val lightMutedColor = palette.getLightMutedColor(darkMutedColor)
            val darkVibrant = palette.getDarkVibrantColor(lightMutedColor)
            val lightVibrant = palette.getLightVibrantColor(darkVibrant)
            val mutedColor = palette.getMutedColor(lightVibrant)
            return palette.getVibrantColor(mutedColor)
        }
        false -> {
            val lightMutedColor = palette.getLightMutedColor(default)
            val lightVibrant = palette.getLightVibrantColor(lightMutedColor)
            val mutedColor = palette.getMutedColor(lightVibrant)
            val darkMutedColor = palette.getDarkMutedColor(mutedColor)
            val vibrant = palette.getVibrantColor(darkMutedColor)
            return palette.getDarkVibrantColor(vibrant)
        }
    }
}

private fun gradientShift(isDarkMode: Boolean, color: Int, shift: Float, alpha: Int): Color {
    return Color(
        if (isDarkMode) shiftColor(color, shift) else ColorUtils.setAlphaComponent(
            shiftColor(color, 2f),
            alpha
        )
    )
}

fun Color.contentColor() = getContrastColor(toArgb()).run { Color(this) }

fun getContrastColor(@ColorInt color: Int): Int {
    // Counting the perceptive luminance - human eye favors green color...
    val a: Double =
        1 - (0.299 * AColor.red(color) + 0.587 * AColor.green(color) + 0.114 * AColor.blue(color)) / 255
    return if (a < 0.5) AColor.BLACK else AColor.WHITE
}

private fun desaturate(isDarkMode: Boolean, color: Int): Int {
    if (!isDarkMode) {
        return color
    }

    if (color == AColor.TRANSPARENT) {
        // can't desaturate transparent color
        return color
    }
    val amount = .25f
    val minDesaturation = .75f

    val hsl = floatArrayOf(0f, 0f, 0f)
    ColorUtils.colorToHSL(color, hsl)
    if (hsl[1] > minDesaturation) {
        hsl[1] = MathUtils.clamp(
            hsl[1] - amount,
            minDesaturation - 0.1f,
            1f
        )
    }
    return ColorUtils.HSLToColor(hsl)
}

fun shiftColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 2.0) by: Float): Int {
    return if (by == 1.0f) {
        color
    } else {
        val alpha = AColor.alpha(color)
        val hsv = FloatArray(3)
        AColor.colorToHSV(color, hsv)
        hsv[2] *= by
        (alpha shl 24) + (16777215 and AColor.HSVToColor(hsv))
    }
}

private fun Pair<Color, Color>.mergeColors(): Color {
    val a = first
    val b = second
    var r = Color.Black

    r = r.copy(alpha = 1 - (1 - b.alpha) * (1 - a.alpha))
    r = r.copy(red = b.red * b.alpha / r.alpha + a.red * a.alpha * (1 - b.alpha) / r.alpha)
    r = r.copy(green = b.green * b.alpha / r.alpha + a.green * a.alpha * (1 - b.alpha) / r.alpha)
    r = r.copy(blue = b.blue * b.alpha / r.alpha + a.blue * a.alpha * (1 - b.alpha) / r.alpha)
    return r
}
