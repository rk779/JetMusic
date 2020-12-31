package wtf.rk585.able.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Green400 = Color(0xFFB8CD7E)
val Green200 = Color(0xFFB8D9A4)
val Jaguar = Color(0xFF202030)

@Composable
val Divider: Color
    get() = BackgroundElevated

@Composable
val BackgroundElevated: Color
    get() = MaterialTheme.colors.onBackground.copy(0.05F)