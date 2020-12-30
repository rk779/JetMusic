package wtf.rk585.able.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun HomeScreen() {
    MainTitle()
}

@Composable
fun MainTitle() {
    Text(
        text = "Able",
        fontSize = TextUnit.Companion.Sp(32),
        textAlign = TextAlign.Center
    )
}