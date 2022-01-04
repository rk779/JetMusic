package tech.rk585.vivace.ui.common.compose.theme

import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun borderlessTextFieldColors(
    backgroundColor: Color = Color.Transparent,
    cursorColor: Color = MaterialTheme.colors.secondary,
) = outlinedTextFieldColors(backgroundColor, cursorColor, Color.Transparent, Color.Transparent)

@Composable
fun outlinedTextFieldColors(
    backgroundColor: Color = Color.Transparent,
    cursorColor: Color = MaterialTheme.colors.secondary,
    focusedBorderColor: Color = MaterialTheme.colors.secondary.copy(alpha = ContentAlpha.medium),
    unfocusedBorderColor: Color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
) = TextFieldDefaults.outlinedTextFieldColors(
    backgroundColor = backgroundColor,
    focusedBorderColor = focusedBorderColor,
    unfocusedBorderColor = unfocusedBorderColor,
    cursorColor = cursorColor,
)

@Composable
fun topAppBarTitleStyle() = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
