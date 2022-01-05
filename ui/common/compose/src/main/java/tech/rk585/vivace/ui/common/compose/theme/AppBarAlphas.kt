package tech.rk585.vivace.ui.common.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

object AppBarAlphas {
    @Composable
    fun translucentBarAlpha(): Float = when {
        MaterialTheme.colors.isLight -> 0.97f
        else -> 0.95f
    }
}
