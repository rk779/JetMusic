package wtf.rk585.able.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import wtf.rk585.able.ui.theme.BackgroundElevated
import wtf.rk585.able.ui.theme.Divider

@OptIn(ExperimentalLayout::class)
@Composable
fun ChipRow(items: List<String>, onChipClicked: (String) -> Unit = {}) {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 16.dp) {
            items.forEach {
                Chip(text = it, onSelected = { onChipClicked(it) })
            }
        }
    }
}

@OptIn(ExperimentalLayout::class)
@Composable
fun SelectableChipRow(items: Array<String>, selectedIndex: Int, onSelectionChanged: (Int) -> Unit) {
    Box(
            modifier = Modifier.border(
                    width = 1.dp,
                    color = Divider,
                    shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row {
            items.forEachIndexed { i, text ->
                Row(Modifier.preferredHeight(32.dp)) {
                    Chip(text = text, selected = i == selectedIndex, onSelected = {
                        onSelectionChanged(i)
                    }, colorNormal = Color.Transparent)
                    if (i < items.size - 1) {
                        CustomDivider(vertical = true, startIndent = 8.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(
        text: String,
        selected: Boolean = false,
        colorSelected: Color = MaterialTheme.colors.secondary.copy(0.4f),
        colorNormal: Color = BackgroundElevated,
        onSelected: () -> Unit
) {
    Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (selected) colorSelected else colorNormal
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clickable(onClick = { onSelected() }).preferredHeight(32.dp)
        ) {
            Text(
                    text = text,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}