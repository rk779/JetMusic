package ml.rk585.jetmusic.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import ml.rk585.jetmusic.data.service.MusicService
import ml.rk585.jetmusic.ui.components.IconButton
import ml.rk585.jetmusic.ui.components.JetImage
import ml.rk585.jetmusic.ui.components.rememberCurrentMediaItem
import ml.rk585.jetmusic.ui.components.rememberMediaSessionPlayer
import ml.rk585.jetmusic.ui.components.rememberPlayProgress
import ml.rk585.jetmusic.util.adaptiveColor
import ml.rk585.jetmusic.util.playPause

@Composable
fun MiniPlayerControls(
    modifier: Modifier = Modifier,
    openPlayerSheet: () -> Unit
) {
    val mediaPlayer by rememberMediaSessionPlayer(MusicService::class.java)

    mediaPlayer?.let { player ->
        val mediaItem = rememberCurrentMediaItem(player)

        AnimatedVisibility(
            visible = mediaItem != MediaItem.EMPTY,
            modifier = modifier,
            enter = expandVertically(expandFrom = Alignment.Bottom),
            exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            MiniPlayerControls(
                player = player,
                openPlayerSheet = openPlayerSheet,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MiniPlayerControls(
    player: Player,
    modifier: Modifier = Modifier,
    openPlayerSheet: () -> Unit
) {
    var currentMediaMetadata by remember(player) { mutableStateOf(player.mediaMetadata) }
    var isMusicPlaying by remember(player) { mutableStateOf(player.isPlaying) }
    val playbackProgress by rememberPlayProgress(player)

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isMusicPlaying = isPlaying
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                currentMediaMetadata = mediaMetadata
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    MiniPlayerControls(
        modifier = modifier,
        isPlaying = isMusicPlaying,
        mediaMetadata = currentMediaMetadata,
        playbackProgress = playbackProgress,
        onPlayPause = player::playPause,
        openPlayerSheet = openPlayerSheet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MiniPlayerControls(
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    isPlaying: Boolean,
    mediaMetadata: MediaMetadata,
    playbackProgress: Pair<Long, Long>?,
    onPlayPause: () -> Unit,
    openPlayerSheet: () -> Unit
) {
    val adaptiveColor by adaptiveColor(
        mediaMetadata.artworkUri,
        MaterialTheme.colorScheme.onBackground
    )
    val backgroundColor by animateColorAsState(adaptiveColor.color)
    val contentColor by animateColorAsState(adaptiveColor.contentColor)
    var dragOffset by remember { mutableStateOf(0f) }

    Surface(
        onClick = openPlayerSheet,
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .animateContentSize()
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState(
                    onDelta = { dragOffset = it.coerceAtMost(0f) }
                ),
                onDragStarted = {
                    if (dragOffset < 0) openPlayerSheet()
                }
            )
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
            ) {
                MiniPlayerInfoWithCover(
                    mediaMetadata = mediaMetadata,
                    maxHeight = height
                )
                PlayPauseButton(
                    isPlaying = isPlaying,
                    onPlayPause = onPlayPause
                )
            }
            MiniPlayerProgressIndicator(
                progress = playbackProgress,
                modifier = Modifier.fillMaxWidth(),
                color = LocalContentColor.current
            )
        }
    }
}

@Composable
private fun RowScope.MiniPlayerInfoWithCover(
    mediaMetadata: MediaMetadata,
    modifier: Modifier = Modifier,
    maxHeight: Dp,
) {
    Row(
        modifier = modifier.weight(3f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        JetImage(
            data = mediaMetadata.artworkData ?: mediaMetadata.artworkUri,
            size = maxHeight - 16.dp,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(8.dp)
        )
        MiniPlayerMusicInfo(
            mediaMetadata = mediaMetadata,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
private fun MiniPlayerMusicInfo(
    mediaMetadata: MediaMetadata,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = mediaMetadata.title.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = mediaMetadata.artist.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    size: Dp = 36.dp,
    onPlayPause: () -> Unit
) {
    IconButton(
        onClick = onPlayPause,
        rippleColor = LocalContentColor.current,
        modifier = modifier
    ) {
        Icon(
            imageVector = when (isPlaying) {
                true -> Icons.Filled.Pause
                false -> Icons.Filled.PlayArrow
            },
            contentDescription = null,
            modifier = Modifier.size(size)
        )
    }
}

@Composable
private fun MiniPlayerProgressIndicator(
    progress: Pair<Long, Long>?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val percent: Float = progress?.let { (it.first * 100f / it.second) / 100f } ?: 0f
    val animatedProgress by animateFloatAsState(targetValue = percent)

    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = Modifier
            .height(2.dp)
            .fillMaxWidth()
            .then(modifier),
        color = color,
        trackColor = color.copy(0.12f)
    )
}
