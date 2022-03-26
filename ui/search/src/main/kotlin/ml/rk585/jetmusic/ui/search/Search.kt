package ml.rk585.jetmusic.ui.search

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.SearchTextField
import ml.rk585.jetmusic.ui.common.components.SmallTopAppBar
import ml.rk585.jetmusic.ui.common.theme.textFieldColors

@Destination
@Composable
fun Search() {
    Search(
        onUpdateQuery = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Search(
    onUpdateQuery: (String) -> Unit
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchTopAppBar(
                scrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxWidth(),
                onUpdateQuery = onUpdateQuery
            )
        }
    ) {

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun SearchTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onUpdateQuery: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    SmallTopAppBar(
        title = { Text(text = stringResource(id = R.string.search)) },
        modifier = modifier,
        scrollBehavior = scrollBehavior
    ) {
        SearchTextField(
            value = searchQuery,
            onValueChange = { value ->
                searchQuery = value
                onUpdateQuery(value.text)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = stringResource(id = R.string.search_placeholder),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            shape = RoundedCornerShape(24.dp),
            colors = textFieldColors()
        )
    }
}
