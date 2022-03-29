package ml.rk585.jetmusic.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.MediaListItem
import ml.rk585.jetmusic.ui.common.components.SearchTextField
import ml.rk585.jetmusic.ui.common.components.SmallTopAppBar
import ml.rk585.jetmusic.ui.common.components.pagerTabIndicatorOffset
import ml.rk585.jetmusic.ui.common.theme.textFieldColors
import org.schabi.newpipe.extractor.InfoItem

@Destination
@Composable
fun Search(
    navigator: SearchNavigator
) {
    Search(
        navigator = navigator,
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun Search(
    navigator: SearchNavigator,
    viewModel: SearchViewModel
) {
    val albumPagingItems = viewModel.albumsPagerFlow.collectAsLazyPagingItems()
    val artistPagingItems = viewModel.artistsPagerFlow.collectAsLazyPagingItems()
    val playlistPagingItems = viewModel.playlistsPagerFlow.collectAsLazyPagingItems()
    val songPagingItems = viewModel.songsPagerFlow.collectAsLazyPagingItems()

    Search(
        albumPagingItems = albumPagingItems,
        artistPagingItems = artistPagingItems,
        playlistPagingItems = playlistPagingItems,
        songPagingItems = songPagingItems,
        onClickArtist = navigator::openArtist,
        onClickPlaylist = navigator::openPlaylist,
        onUpdateQuery = viewModel::updateQuery
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class
)
@Composable
internal fun Search(
    albumPagingItems: LazyPagingItems<InfoItem>,
    artistPagingItems: LazyPagingItems<InfoItem>,
    playlistPagingItems: LazyPagingItems<InfoItem>,
    songPagingItems: LazyPagingItems<InfoItem>,
    onClickArtist: (String) -> Unit,
    onClickPlaylist: (String) -> Unit,
    onUpdateQuery: (String) -> Unit
) {
    val pagerState = rememberPagerState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchTopAppBar(
                pagerState = pagerState,
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                onUpdateQuery = onUpdateQuery
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            count = PAGES.size,
            state = pagerState,
            contentPadding = paddingValues
        ) { page ->
            Crossfade(targetState = page) { state ->
                when (state) {
                    0 -> ListPage(songPagingItems)
                    1 -> ListPage(albumPagingItems, onClickPlaylist = onClickPlaylist)
                    2 -> ListPage(artistPagingItems, onClickArtist = onClickArtist)
                    3 -> ListPage(playlistPagingItems, onClickPlaylist = onClickPlaylist)
                }
            }
        }
    }
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalPagerApi::class
)
@Composable
internal fun SearchTopAppBar(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onUpdateQuery: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = stringResource(id = R.string.search_placeholder),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            shape = RoundedCornerShape(24.dp),
            colors = textFieldColors()
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(
                        pagerState = pagerState,
                        tabPositions = tabPositions
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            PAGES.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun <T : InfoItem> ListPage(
    pagingItems: LazyPagingItems<T>,
    onClickArtist: ((String) -> Unit)? = null,
    onClickPlaylist: ((String) -> Unit)? = null
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(pagingItems) { item ->
            if (item != null) {
                MediaListItem(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    onClickArtist = onClickArtist,
                    onClickPlaylist = onClickPlaylist,
                )
            }
        }
    }
}

private val PAGES = listOf("Songs", "Albums", "Artists", "Playlists")
