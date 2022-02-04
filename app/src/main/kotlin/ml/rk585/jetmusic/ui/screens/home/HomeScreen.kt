package ml.rk585.jetmusic.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.data.model.SearchQuery
import ml.rk585.jetmusic.ui.components.JetMusicBottomNavigationBar
import ml.rk585.jetmusic.ui.components.rememberFlowWithLifecycle
import ml.rk585.jetmusic.ui.screens.home.pages.LibraryPage
import ml.rk585.jetmusic.ui.screens.home.pages.SearchPage
import ml.rk585.jetmusic.ui.screens.player.MiniPlayerControls

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    openPlayer: () -> Unit,
    openPlaylistDetail: (String) -> Unit
) {
    val pagerState = rememberPagerState()

    val viewModel: HomeViewModel = hiltViewModel()
    val items by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = emptyList())
    val searchQuery by rememberFlowWithLifecycle(viewModel.query)
        .collectAsState(initial = SearchQuery())

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayerControls(
                    openPlayerSheet = openPlayer,
                    modifier = Modifier.fillMaxWidth()
                )
                BottomNavigationBar(
                    pagerState = pagerState,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { insetPaddingValues ->
        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(insetPaddingValues)
        ) { page ->
            when (page) {
                0 -> {
                    SearchPage(
                        modifier = Modifier.fillMaxSize(),
                        searchQuery = searchQuery,
                        onUpdateQuery = viewModel::updateQuery,
                        onUpdateType = viewModel::updateSearchType,
                        onPlayMusic = viewModel::playMusic,
                        onPlaylistDetail = openPlaylistDetail,
                        items = items
                    )
                }
                1 -> {
                    LibraryPage(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomNavigationBar(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "Search" to Icons.Default.Search,
        "Library" to Icons.Default.LibraryMusic
    )
    val scope = rememberCoroutineScope()

    JetMusicBottomNavigationBar(
        modifier = modifier
    ) {
        items.forEachIndexed { index, pair ->
            NavigationBarItem(
                selected = index == pagerState.currentPage,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                icon = {
                    Icon(
                        imageVector = pair.second,
                        contentDescription = pair.first
                    )
                },
                label = { Text(text = pair.first) }
            )
        }
    }
}
