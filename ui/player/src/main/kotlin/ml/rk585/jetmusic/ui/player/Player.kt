package ml.rk585.jetmusic.ui.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import ml.rk585.jetmusic.core.media.MusicPlayer
import ml.rk585.jetmusic.ui.common.LocalMusicPlayer
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.AppBarStyle
import ml.rk585.jetmusic.ui.common.components.JetMusicTopAppBar
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle
import ml.rk585.jetmusic.ui.common.utils.ADAPTIVE_COLOR_ANIMATION
import ml.rk585.jetmusic.ui.common.utils.adaptiveColor
import ml.rk585.jetmusic.ui.player.components.PlaybackArtworkWithNowPlayingAndControls

@Destination(style = DestinationStyle.BottomSheet::class)
@Composable
fun Player(
    navigator: PlayerNavigator
) {
    Player(
        onDismiss = navigator::onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Player(
    musicPlayer: MusicPlayer = LocalMusicPlayer.current,
    onDismiss: () -> Unit
) {
    val mediaItem by rememberStateWithLifecycle(musicPlayer.currentMediaItem)
    val adaptiveColor by adaptiveColor(
        imageData = mediaItem.mediaMetadata.artworkData ?: mediaItem.mediaMetadata.artworkUri,
        fallback = MaterialTheme.colorScheme.onBackground,
        gradientEndColor = MaterialTheme.colorScheme.surface
    )
    val contentColor by animateColorAsState(adaptiveColor.color, ADAPTIVE_COLOR_ANIMATION)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(adaptiveColor.gradient),
        topBar = {
            PlayerTopAppBar(
                mediaMetadata = mediaItem.mediaMetadata,
                scrollBehavior = scrollBehavior,
                onDismiss = onDismiss
            )
        },
        containerColor = Color.Transparent,
        contentColor = contentColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                PlaybackArtworkWithNowPlayingAndControls(
                    musicPlayer = musicPlayer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .fillParentMaxHeight(0.8f)
                )
            }
        }
    }
}

@Composable
internal fun PlayerTopAppBar(
    modifier: Modifier = Modifier,
    mediaMetadata: MediaMetadata = MediaMetadata.EMPTY,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onDismiss: () -> Unit
) {
    JetMusicTopAppBar(
        title = { PlayerTopAppBarTitleContent(mediaMetadata) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.navigate_up)
                )
            }
        },
        appBarStyle = AppBarStyle.Center,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
internal fun PlayerTopAppBarTitleContent(
    mediaMetadata: MediaMetadata,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mediaMetadata.title.toString(),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = mediaMetadata.artist.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
