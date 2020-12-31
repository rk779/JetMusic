package wtf.rk585.able.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import wtf.rk585.able.R
import wtf.rk585.able.ui.components.SelectableChipRow

@Composable
fun SearchScreen() {

    val searchInputState = remember { mutableStateOf(TextFieldValue()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
                text = "Search",
                fontWeight = Bold,
                fontSize = TextUnit.Companion.Sp(32),
                modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
                value = searchInputState.value,
                onValueChange = {
                    searchInputState.value = it
                },
                label = { Text("Enter search query") },
                modifier = Modifier.fillMaxWidth().padding(
                        bottom = 16.dp
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                onImeActionPerformed = { _, controller ->
                    controller?.hideSoftwareKeyboard()
                },
                trailingIcon = { Icon(Icons.Rounded.Search) }
        )
        SelectableChipRow(
                items = stringArrayResource(id = R.array.search_filter),
                selectedIndex = 0
        ) {

        }
    }
}