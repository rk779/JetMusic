package wtf.rk585.able.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import wtf.rk585.able.ui.theme.Divider

@Composable
fun CustomDivider(
        modifier: Modifier = Modifier,
        color: Color = Divider,
        thickness: Dp = 1.dp,
        startIndent: Dp = 0.dp,
        vertical: Boolean = false
) {
    val indentMod = if (startIndent.value != 0f) {
        if (vertical) Modifier.padding(vertical = startIndent) else Modifier.padding(start = startIndent)
    } else {
        Modifier
    }
    if (vertical) {
        Box(modifier.then(indentMod).fillMaxHeight().preferredWidth(thickness).background(color))
    } else {
        Box(modifier.then(indentMod).fillMaxWidth().preferredHeight(thickness).background(color))
    }
}