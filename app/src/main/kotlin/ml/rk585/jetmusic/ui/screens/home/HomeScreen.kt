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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.data.model.SearchQuery
import ml.rk585.jetmusic.ui.components.JetMusicBottomNavigationBar
import ml.rk585.jetmusic.ui.components.rememberFlowWithLifecycle
import ml.rk585.jetmusic.ui.screens.destinations.ArtistScreenDestination
import ml.rk585.jetmusic.ui.screens.destinations.PlayerSheetDestination
import ml.rk585.jetmusic.ui.screens.destinations.PlaylistScreenDestination
import ml.rk585.jetmusic.ui.screens.home.pages.LibraryPage
import ml.rk585.jetmusic.ui.screens.home.pages.SearchPage
import ml.rk585.jetmusic.ui.screens.player.MiniPlayerControls
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem

@Destination(
    start = true
)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    HomeScreen(
        viewModel = hiltViewModel(),
        onArtistDetail = { channelInfoItem ->
            navigator.navigate(
                ArtistScreenDestination(
                    artistName = channelInfoItem.name,
                    artistUrl = channelInfoItem.url,
                    artworkUrl = channelInfoItem.thumbnailUrl
                )
            )
        },
        openPlayer = {
            navigator.navigate(PlayerSheetDestination)
        },
        openPlaylistDetail = { playlistInfoItem ->
            navigator.navigate(
                PlaylistScreenDestination(
                    name = playlistInfoItem.name,
                    artistName = playlistInfoItem.uploaderName,
                    artworkUrl = playlistInfoItem.thumbnailUrl,
                    playlistUrl = playlistInfoItem.url
                )
            )
        }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class
)
@Composable
private fun HomeScreen(
    viewModel: HomeViewModel,
    onArtistDetail: (ChannelInfoItem) -> Unit,
    openPlayer: () -> Unit,
    openPlaylistDetail: (PlaylistInfoItem) -> Unit
) {
    val pagerState = rememberPagerState()

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
                        onArtistDetail = onArtistDetail,
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
