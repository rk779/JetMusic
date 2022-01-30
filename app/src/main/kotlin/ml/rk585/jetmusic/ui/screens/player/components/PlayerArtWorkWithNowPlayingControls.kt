package ml.rk585.jetmusic.ui.screens.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import ml.rk585.jetmusic.ui.components.IconButton
import ml.rk585.jetmusic.ui.components.JetImage
import ml.rk585.jetmusic.ui.components.Slider
import ml.rk585.jetmusic.ui.components.m3Colors
import ml.rk585.jetmusic.ui.components.rememberCurrentMediaItem
import ml.rk585.jetmusic.ui.components.rememberPlayProgress
import ml.rk585.jetmusic.util.formatAsPlayerTime
import ml.rk585.jetmusic.util.playPause
import ml.rk585.jetmusic.util.toggleRepeatMode
import ml.rk585.jetmusic.util.toggleShuffleMode
import kotlin.math.roundToLong

@Composable
fun PlaybackArtworkPagerWithNowPlayingAndControls(
    modifier: Modifier = Modifier,
    player: Player
) {
    val currentMediaItem = rememberCurrentMediaItem(player)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JetImage(
            data = currentMediaItem.mediaMetadata.artworkUri,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlayerNowPlayingInfo(
            mediaMetadata = currentMediaItem.mediaMetadata,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlayerProgressSlider(
            player = player,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlaybackControls(
            player = player,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PlaybackControls(
    modifier: Modifier = Modifier,
    player: Player
) {
    var isMusicPlaying by remember(player) { mutableStateOf(player.isPlaying) }
    var isShuffleModeEnabled by remember(player) { mutableStateOf(player.shuffleModeEnabled) }
    var isRepeatModeEnabled by remember(player) { mutableStateOf(player.repeatMode) }
    val hasNextMediaItem by remember(isRepeatModeEnabled) { mutableStateOf(player.hasNextMediaItem()) }
    val hasPreviousMediaItem by remember(isRepeatModeEnabled) { mutableStateOf(player.hasPreviousMediaItem()) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isMusicPlaying = isPlaying
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                isRepeatModeEnabled = repeatMode
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                isShuffleModeEnabled = shuffleModeEnabled
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { player.toggleShuffleMode() },
            modifier = Modifier
                .size(20.dp)
                .weight(2f)
        ) {
            Icon(
                imageVector = when {
                    isShuffleModeEnabled -> Icons.Default.ShuffleOn
                    else -> Icons.Default.Shuffle
                },
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(28.dp))
        IconButton(
            onClick = { player.seekToPreviousMediaItem() },
            enabled = hasPreviousMediaItem,
            modifier = Modifier
                .size(40.dp)
                .weight(4f),
            rippleRadius = 30.dp
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(16.dp))
        IconButton(
            onClick = { player.playPause() },
            modifier = Modifier
                .size(80.dp)
                .weight(8f),
            rippleRadius = 40.dp
        ) {
            Icon(
                imageVector = when (isMusicPlaying) {
                    true -> Icons.Filled.PauseCircleFilled
                    false -> Icons.Filled.PlayCircleFilled
                },
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(16.dp))
        IconButton(
            onClick = { player.seekToNextMediaItem() },
            enabled = hasNextMediaItem,
            modifier = Modifier
                .size(40.dp)
                .weight(4f),
            rippleRadius = 30.dp
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(28.dp))
        IconButton(
            onClick = { player.toggleRepeatMode() },
            modifier = Modifier
                .size(20.dp)
                .weight(2f)
        ) {
            Icon(
                imageVector = when (isRepeatModeEnabled) {
                    Player.REPEAT_MODE_OFF -> Icons.Default.Repeat
                    Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
                    Player.REPEAT_MODE_ALL -> Icons.Default.RepeatOn
                    else -> Icons.Default.Repeat
                },
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PlayerNowPlayingInfo(
    mediaMetadata: MediaMetadata,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = mediaMetadata.title.toString(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = mediaMetadata.artist.toString(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PlayerProgressSlider(
    player: Player,
    modifier: Modifier = Modifier
) {
    val progress by rememberPlayProgress(player)
    val percent: Float = progress?.let { (it.first * 100f / it.second) / 100f } ?: 0f
    val (value, onValueChange) = remember(percent) { mutableStateOf(percent) }

    Box(
        modifier = modifier,
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            onValueChangeFinished = {
                player.seekTo(
                    (value * (progress?.second ?: 0L))
                        .roundToLong()
                        .coerceAtLeast(0L)
                )
            },
            colors = SliderDefaults.m3Colors(
                thumbColor = LocalContentColor.current,
                activeTrackColor = LocalContentColor.current,
                inactiveTrackColor = LocalContentColor.current.copy(0.12f)
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = progress?.first?.formatAsPlayerTime() ?: "00:00",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = progress?.second?.formatAsPlayerTime() ?: "00:00",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
