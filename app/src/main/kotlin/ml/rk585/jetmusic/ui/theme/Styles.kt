package ml.rk585.jetmusic.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textFieldColors(
    textColor: Color = LocalContentColor.current,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(0.5f),
    iconColor: Color = MaterialTheme.colorScheme.primary.copy(0.54f),
): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        textColor = textColor,
        backgroundColor = backgroundColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = iconColor,
        focusedTrailingIconColor = iconColor
    )
}
