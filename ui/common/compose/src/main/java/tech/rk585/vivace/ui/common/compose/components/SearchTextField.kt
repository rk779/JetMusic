package tech.rk585.vivace.ui.common.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import tech.rk585.vivace.ui.common.compose.R
import tech.rk585.vivace.ui.common.compose.theme.borderlessTextFieldColors

@Composable
fun SearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    autoFocus: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    hint: String = stringResource(R.string.search_hint_query),
    maxLength: Int = 200,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Search,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Sentences
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val focusRequester = remember { FocusRequester() }

    DisposableEffect(autoFocus) {
        if (autoFocus) focusRequester.requestFocus()
        onDispose { }
    }

    OutlinedTextField(
        value = value,
        leadingIcon = {
            Icon(
                tint = MaterialTheme.colors.onBackground,
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.nav_search)
            )
        },
        onValueChange = { if (it.text.length <= maxLength) onValueChange(it) },
        placeholder = { Text(text = hint, style = textStyle) },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.text.isNotEmpty(),
                enter = expandIn(expandFrom = Alignment.Center),
                exit = shrinkOut(shrinkTowards = Alignment.Center)
            ) {
                IconButton(
                    onClick = {
                        onValueChange(TextFieldValue())
                    },
                ) {
                    Icon(
                        tint = MaterialTheme.colors.secondary,
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.generic_clear)
                    )
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        colors = borderlessTextFieldColors(
            backgroundColor = MaterialTheme.colors.onSurface.copy(0.12f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}
