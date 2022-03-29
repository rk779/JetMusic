package ml.rk585.jetmusic.ui.player.miniPlayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import ml.rk585.jetmusic.core.media.MusicPlayer
import ml.rk585.jetmusic.ui.common.LocalMusicPlayer
import ml.rk585.jetmusic.ui.common.components.IconButton
import ml.rk585.jetmusic.ui.common.components.JetImage
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle
import ml.rk585.jetmusic.ui.common.utils.adaptiveColor
import ml.rk585.jetmusic.ui.player.components.animatePlaybackProgress

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    openPlayerSheet: () -> Unit
) {
    val musicPlayer = LocalMusicPlayer.current
    val currentMediaItem by rememberStateWithLifecycle(musicPlayer.currentMediaItem)

    AnimatedVisibility(
        visible = currentMediaItem != MediaItem.EMPTY,
        modifier = modifier,
        enter = expandVertically(expandFrom = Alignment.Bottom),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        MiniPlayer(
            musicPlayer = musicPlayer,
            openPlayerSheet = openPlayerSheet,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MiniPlayer(
    musicPlayer: MusicPlayer,
    modifier: Modifier = Modifier,
    openPlayerSheet: () -> Unit
) {
    val currentMediaItem by rememberStateWithLifecycle(musicPlayer.currentMediaItem)
    val isPlaying by rememberStateWithLifecycle(musicPlayer.isPlaying)

    MiniPlayer(
        modifier = modifier,
        isPlaying = isPlaying,
        mediaMetadata = currentMediaItem.mediaMetadata,
        onPlayPause = musicPlayer::pauseOrResume,
        openPlayerSheet = openPlayerSheet
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MiniPlayer(
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    isPlaying: Boolean,
    mediaMetadata: MediaMetadata,
    onPlayPause: () -> Unit,
    openPlayerSheet: () -> Unit
) {
    val adaptiveColor by adaptiveColor(
        mediaMetadata.artworkUri,
        initial = MaterialTheme.colorScheme.background
    )
    val containerColor by animateColorAsState(adaptiveColor.color)
    val contentColor by animateColorAsState(adaptiveColor.contentColor)
    var dragOffset by remember { mutableStateOf(0f) }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 4.dp)
            .animateContentSize()
            .combinedClickable(
                enabled = true,
                onClick = openPlayerSheet,
                onLongClick = onPlayPause,
                onDoubleClick = onPlayPause
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState(
                    onDelta = { value ->
                        dragOffset = value.coerceAtMost(0f)
                    }
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
                    maxHeight = height,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                PlayPauseButton(
                    isPlaying = isPlaying,
                    onPlayPause = onPlayPause
                )
            }
            MiniPlayerProgressIndicator()
        }
    }
}

@Composable
private fun MiniPlayerInfoWithCover(
    mediaMetadata: MediaMetadata,
    modifier: Modifier = Modifier,
    maxHeight: Dp,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JetImage(
            data = mediaMetadata.artworkData ?: mediaMetadata.artworkUri,
            size = maxHeight - 16.dp,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        MiniPlayerMusicInfo(
            mediaMetadata = mediaMetadata,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun MiniPlayerMusicInfo(
    mediaMetadata: MediaMetadata,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
    contentColor: Color = LocalContentColor.current,
    musicPlayer: MusicPlayer = LocalMusicPlayer.current
) {
    val playbackProgress by rememberStateWithLifecycle(musicPlayer.playbackProgress)
    val animatedProgress by animatePlaybackProgress(playbackProgress.progress)
    val playbackState by rememberStateWithLifecycle(musicPlayer.playbackState)
    val lineageProgressModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .height(2.dp)
        .clip(CircleShape)

    when (playbackState) {
        Player.STATE_BUFFERING -> {
            LinearProgressIndicator(
                color = contentColor,
                modifier = lineageProgressModifier
            )
        }
        else -> {
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = lineageProgressModifier,
                color = contentColor,
                trackColor = contentColor.copy(0.12f)
            )
        }
    }
}
