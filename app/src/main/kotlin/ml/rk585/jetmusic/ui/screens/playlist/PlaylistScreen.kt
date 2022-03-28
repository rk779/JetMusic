package ml.rk585.jetmusic.ui.screens.playlist

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ml.rk585.jetmusic.R
import ml.rk585.jetmusic.data.model.StreamsCountDuration
import ml.rk585.jetmusic.ui.common.components.AppBarStyle
import ml.rk585.jetmusic.ui.common.components.JetMusicTopAppBar
import ml.rk585.jetmusic.ui.common.components.MusicListItem
import ml.rk585.jetmusic.ui.common.components.PlayPauseButton
import ml.rk585.jetmusic.ui.common.components.ScaledCoverImage
import ml.rk585.jetmusic.ui.common.components.rememberFlowWithLifecycle
import ml.rk585.jetmusic.ui.common.components.scaledCoverImageScrollProgress
import ml.rk585.jetmusic.ui.common.utils.LocalAdaptiveColorResult
import ml.rk585.jetmusic.ui.common.utils.adaptiveColor
import kotlin.time.Duration.Companion.seconds

@Destination(navArgsDelegate = PlaylistScreenNavArgs::class)
@Composable
fun PlaylistScreen(
    navigator: DestinationsNavigator,
    navArgs: PlaylistScreenNavArgs
) {
    PlaylistScreen(
        navArgs = navArgs,
        viewModel = hiltViewModel(),
        onNavigateUp = navigator::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistScreen(
    navArgs: PlaylistScreenNavArgs,
    viewModel: PlaylistViewModel,
    onNavigateUp: () -> Unit
) {
    val adaptiveColor by adaptiveColor(
        navArgs.artworkUrl,
        fallback = MaterialTheme.colorScheme.background,
        gradientEndColor = MaterialTheme.colorScheme.background
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }

    val viewState by rememberFlowWithLifecycle(viewModel.uiState)
        .collectAsState(initial = PlaylistViewState.Empty)

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(adaptiveColor.gradient),
        topBar = {
            PlaylistTopAppBar(
                name = navArgs.name,
                scrollBehavior = scrollBehavior,
                onNavigateUp = onNavigateUp
            )
        },
        bottomBar = {
            Spacer(modifier = Modifier.navigationBarsPadding())
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = Color.Transparent
    ) { contentPadding ->
        CompositionLocalProvider(
            LocalAdaptiveColorResult provides adaptiveColor
        ) {
            PlaylistContent(
                navArgs = navArgs,
                viewState = viewState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                onPlayAll = { viewModel.playAll(viewState.items) }
            )
        }
    }
}

@Composable
private fun PlaylistTopAppBar(
    name: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateUp: () -> Unit
) {
    JetMusicTopAppBar(
        title = {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        appBarStyle = AppBarStyle.Small,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun PlaylistContent(
    navArgs: PlaylistScreenNavArgs,
    viewState: PlaylistViewState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onPlayAll: () -> Unit
) {
    val listState = rememberLazyListState()
    val coverOffsetProgress by scaledCoverImageScrollProgress(listState)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
        state = listState
    ) {
        item {
            ScaledCoverImage(
                modifier = Modifier.fillMaxWidth(),
                imageData = navArgs.artworkUrl,
                icon = rememberVectorPainter(Icons.Default.Album),
                offsetProgress = coverOffsetProgress
            )
        }
        item {
            PlaylistHeaderDetails(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                streamsCountDuration = viewState.streamsCountDuration,
                artistName = navArgs.artistName,
                playerLoading = viewState.playerLoading,
                onClick = onPlayAll
            )
        }
        items(viewState.items) { item ->
            MusicListItem(item)
        }
    }
}

@Composable
private fun PlaylistHeaderDetails(
    modifier: Modifier = Modifier,
    artistName: String,
    streamsCountDuration: StreamsCountDuration? = null,
    playerLoading: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = artistName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            streamsCountDuration?.let { count ->
                val text = LocalContext.current.resources.getQuantityString(
                    R.plurals.music_count_duration,
                    count.count,
                    count.count,
                    count.duration.seconds
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        PlayPauseButton(
            onClick = onClick,
            containerColor = LocalAdaptiveColorResult.current.color,
            contentColor = LocalAdaptiveColorResult.current.contentColor
        ) {
            Crossfade(targetState = playerLoading) { state ->
                when {
                    state -> {
                        CircularProgressIndicator(
                            color = LocalContentColor.current
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
