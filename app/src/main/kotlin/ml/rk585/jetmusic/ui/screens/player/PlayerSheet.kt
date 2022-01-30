package ml.rk585.jetmusic.ui.screens.player

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import ml.rk585.jetmusic.data.service.MusicService
import ml.rk585.jetmusic.ui.components.AppBarStyle
import ml.rk585.jetmusic.ui.components.FullScreenLoading
import ml.rk585.jetmusic.ui.components.JetMusicTopAppBar
import ml.rk585.jetmusic.ui.components.rememberCurrentMediaItem
import ml.rk585.jetmusic.ui.components.rememberMediaSessionPlayer
import ml.rk585.jetmusic.ui.screens.player.components.PlaybackArtworkPagerWithNowPlayingAndControls
import ml.rk585.jetmusic.util.ADAPTIVE_COLOR_ANIMATION
import ml.rk585.jetmusic.util.adaptiveColor

@Composable
fun PlayerSheet(
    onClose: () -> Unit
) {
    val player by rememberMediaSessionPlayer(MusicService::class.java)

    when (player != null) {
        true -> {
            PlayerSheetContent(
                player = player!!,
                onClose = onClose
            )
        }
        false -> {
            FullScreenLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerSheetContent(
    player: Player,
    onClose: () -> Unit
) {
    val currentMediaItem = rememberCurrentMediaItem(player)
    val adaptiveColor by adaptiveColor(
        currentMediaItem.mediaMetadata.artworkUri,
        MaterialTheme.colorScheme.onBackground
    )
    val contentColor by animateColorAsState(adaptiveColor.color, ADAPTIVE_COLOR_ANIMATION)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(adaptiveColor.gradient),
        topBar = {
            PlayerSheetTopAppBar(
                mediaMetadata = currentMediaItem.mediaMetadata,
                onClose = onClose
            )
        },
        containerColor = Color.Transparent,
        contentColor = contentColor
    ) { innerPaddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPaddingValues,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PlaybackArtworkPagerWithNowPlayingAndControls(
                    player = player,
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
private fun PlayerSheetTopAppBar(
    mediaMetadata: MediaMetadata,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    JetMusicTopAppBar(
        title = { PlayerSheetTopAppBarTitleContent(mediaMetadata) },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        },
        appBarStyle = AppBarStyle.Center,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
fun PlayerSheetTopAppBarTitleContent(
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
