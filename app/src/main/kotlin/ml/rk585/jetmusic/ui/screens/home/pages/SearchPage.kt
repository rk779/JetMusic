package ml.rk585.jetmusic.ui.screens.home.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.R
import ml.rk585.jetmusic.data.model.SearchQuery
import ml.rk585.jetmusic.data.model.SearchType
import ml.rk585.jetmusic.ui.components.JetImage
import ml.rk585.jetmusic.ui.components.SearchField
import ml.rk585.jetmusic.ui.components.SelectableChipRow
import ml.rk585.jetmusic.ui.components.SmallTopAppBar
import ml.rk585.jetmusic.ui.theme.textFieldColors
import org.schabi.newpipe.extractor.InfoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
    modifier: Modifier = Modifier,
    searchQuery: SearchQuery,
    onUpdateQuery: (String) -> Unit,
    onUpdateType: (SearchType) -> Unit,
    items: List<InfoItem> = emptyList()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .then(modifier),
        topBar = {
            SearchTopAppBar(
                searchQuery = searchQuery,
                onUpdateQuery = onUpdateQuery,
                onUpdateType = onUpdateType,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        DummyList(
            modifier = Modifier.fillMaxSize(),
            items = items,
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTopAppBar(
    searchQuery: SearchQuery,
    onUpdateQuery: (String) -> Unit,
    onUpdateType: (SearchType) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(searchQuery.query))
    }

    SmallTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchField(
            value = query,
            onValueChange = {
                query = it
                onUpdateQuery(it.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            placeholder = "Search",
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            shape = RoundedCornerShape(24.dp),
            colors = textFieldColors()
        )

        SelectableChipRow(
            items = SearchType.values(),
            selectedChip = searchQuery.type,
            onChipSelected = onUpdateType,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DummyList(
    modifier: Modifier = Modifier,
    items: List<InfoItem> = emptyList(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items) { item ->
            ListItem(
                text = {
                    Text(
                        text = item.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                secondaryText = {
                    Text(
                        text = when (item.infoType) {
                            InfoItem.InfoType.STREAM -> "Music"
                            InfoItem.InfoType.PLAYLIST -> "Playlist"
                            InfoItem.InfoType.CHANNEL -> "Channel"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalContentColor.current.copy(ContentAlpha.medium)
                    )
                },
                icon = {
                    JetImage(
                        data = item.thumbnailUrl,
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        size = 48.dp,
                        shape = CircleShape
                    )
                },
                modifier = Modifier.clickable {
                    scope.launch {
                        snackbarHostState.showSnackbar("Function not implemented")
                    }
                }
            )
        }
    }
}
