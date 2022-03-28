package ml.rk585.jetmusic.ui.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ramcosta.composedestinations.annotation.Destination
import ml.rk585.jetmusic.core.base.extensions.orNA
import ml.rk585.jetmusic.core.base.extensions.stripAlbumPrefix
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.FullScreenLoading
import ml.rk585.jetmusic.ui.common.components.IconButton
import ml.rk585.jetmusic.ui.common.components.JetImage
import ml.rk585.jetmusic.ui.common.components.JetMusicTopAppBar
import ml.rk585.jetmusic.ui.common.components.MediaListItem
import ml.rk585.jetmusic.ui.common.components.ScaledCoverImage
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle
import ml.rk585.jetmusic.ui.common.components.scaledCoverImageScrollProgress
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@Destination(navArgsDelegate = PlaylistNavArgs::class)
@Composable
fun Playlist(
    navigator: PlaylistNavigator
) {
    Playlist(
        navigator = navigator,
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun Playlist(
    navigator: PlaylistNavigator,
    viewModel: PlaylistViewModel
) {
    val playlistInfo by rememberStateWithLifecycle(viewModel.playlistInfo)
    val playlistPagingItems = viewModel.playlistPagerFlow.collectAsLazyPagingItems()

    if (playlistInfo != null) {
        Playlist(
            playlistInfo = playlistInfo!!,
            playlistPagingItems = playlistPagingItems,
            onNavigateUp = navigator::onNavigateUp
        )
    } else {
        FullScreenLoading()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Playlist(
    playlistInfo: PlaylistInfo,
    playlistPagingItems: LazyPagingItems<StreamInfoItem>,
    onNavigateUp: () -> Unit
) {
    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PlaylistTopAppBar(
                title = playlistInfo.name.stripAlbumPrefix(),
                scrollBehavior = scrollBehavior,
                onNavigateUp = onNavigateUp
            )
        }
    ) { paddingValues ->
        PlaylistContent(
            playlistInfo = playlistInfo,
            playlistPagingItems = playlistPagingItems,
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
internal fun PlaylistContent(
    playlistInfo: PlaylistInfo,
    playlistPagingItems: LazyPagingItems<StreamInfoItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val listState = rememberLazyListState()
    val coverOffsetProgress by scaledCoverImageScrollProgress(listState)

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = listState
    ) {
        item {
            ScaledCoverImage(
                modifier = Modifier.fillMaxWidth(),
                imageData = playlistInfo.thumbnailUrl,
                icon = rememberVectorPainter(Icons.Default.Album),
                offsetProgress = coverOffsetProgress
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            PlaylistInfoDetailsHeader(
                playlistInfo = playlistInfo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        items(playlistPagingItems) { item ->
            if (item != null) {
                MediaListItem(
                    item = item,
                    onClickArtist = {},
                    onClickPlaylist = {},
                    onClickSong = {}
                )
            }
        }
    }
}

@Composable
internal fun PlaylistInfoDetailsHeader(
    playlistInfo: PlaylistInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                JetImage(
                    data = playlistInfo.uploaderAvatarUrl,
                    contentColor = LocalContentColor.current,
                    contentScale = ContentScale.Crop,
                    backgroundIcon = rememberVectorPainter(Icons.Outlined.Person),
                    size = 20.dp,
                    shape = CircleShape
                )
                Column {
                    Text(
                        text = playlistInfo.uploaderName.orNA(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.song_count,
                            count = playlistInfo.streamCount.toInt(),
                            playlistInfo.streamCount
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null
                    )
                }
            }
        }
        Button(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = stringResource(id = R.string.play))
        }
    }
}

@Composable
internal fun PlaylistTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateUp: () -> Unit
) {
    JetMusicTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_up)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
