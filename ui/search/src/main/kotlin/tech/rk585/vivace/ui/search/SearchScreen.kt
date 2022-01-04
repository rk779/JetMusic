package tech.rk585.vivace.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cf.rk585.vivace.core.base.extensions.interpunctize
import cf.rk585.vivace.core.base.extensions.secondsToDuration
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.insets.ui.Scaffold
import tech.rk585.vivace.ui.common.compose.components.CoverImage
import tech.rk585.vivace.ui.common.compose.components.SearchTextField
import tech.rk585.vivace.ui.common.compose.components.fullScreenLoading
import tech.rk585.vivace.ui.common.compose.components.rememberFlowWithLifecycle
import tech.rk585.vivace.ui.common.compose.theme.topAppBarTitleStyle
import tech.rk585.vivace.ui.common.compose.theme.translucentSurface

@Composable
fun SearchScreen() {
    SearchScreen(
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel
) {
    val state by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = SearchViewState.Empty)

    Search(
        state = state,
        onSearchQueryChanged = viewModel::updateSearch
    )
}

@Composable
internal fun Search(
    state: SearchViewState,
    onSearchQueryChanged: (query: String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchAppBar(
                state = state,
                onSearchQueryChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .translucentSurface()
                    .statusBarsPadding()
            )
        }
    ) {
        SearchList(
            state = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchAppBar(
    state: SearchViewState,
    modifier: Modifier = Modifier,
    onSearchQueryChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(state.query)
        )
    }

    Box(modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_search),
                style = topAppBarTitleStyle(),
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            SearchTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChange(it.text)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SearchList(
    state: SearchViewState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = LocalScaffoldPadding.current,
        modifier = modifier
    ) {
        if (state.refreshing) {
            fullScreenLoading()
        }

        items(state.songs) { item ->
            ListItem(
                text = {
                    Text(
                        text = item.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    CoverImage(
                        data = item.thumbnailUrl,
                        size = 48.dp
                    )
                },
                secondaryText = {
                    val artistAndDuration = listOf(
                        item.uploaderName.toString(),
                        item.duration.secondsToDuration()
                    ).interpunctize()
                    Text(
                        text = artistAndDuration,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                singleLineSecondaryText = true
            )
        }
    }
}
