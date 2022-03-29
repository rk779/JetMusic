package ml.rk585.jetmusic.ui.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import ml.rk585.jetmusic.core.media.MusicPlayer
import ml.rk585.jetmusic.ui.common.LocalMusicPlayer
import ml.rk585.jetmusic.ui.common.components.IconButton
import ml.rk585.jetmusic.ui.common.components.JetImage
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle

@Composable
internal fun PlaybackArtworkWithNowPlayingAndControls(
    modifier: Modifier = Modifier,
    musicPlayer: MusicPlayer = LocalMusicPlayer.current
) {
    val mediaItem by rememberStateWithLifecycle(musicPlayer.currentMediaItem)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        JetImage(
            data = mediaItem.mediaMetadata.artworkUri,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        PlayerNowPlayingInfo(
            mediaMetadata = mediaItem.mediaMetadata,
            modifier = Modifier.fillMaxWidth()
        )
        PlaybackProgress(
            modifier = Modifier.fillMaxWidth()
        )
        PlaybackControls(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun PlaybackControls(
    modifier: Modifier = Modifier,
    musicPlayer: MusicPlayer = LocalMusicPlayer.current
) {
    val playerState by rememberStateWithLifecycle(musicPlayer.playerState)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = musicPlayer::toggleShuffleMode,
            modifier = Modifier
                .size(20.dp)
                .weight(2f)
        ) {
            Icon(
                imageVector = when {
                    playerState.shuffleModeEnabled -> Icons.Default.ShuffleOn
                    else -> Icons.Default.Shuffle
                },
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(28.dp))
        IconButton(
            onClick = musicPlayer::seekToPrevious,
            enabled = musicPlayer.hasPreviousMediaItem(),
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
            onClick = musicPlayer::pauseOrResume,
            modifier = Modifier
                .size(80.dp)
                .weight(8f),
            rippleRadius = 40.dp
        ) {
            Icon(
                imageVector = when (playerState.isPlaying) {
                    true -> Icons.Filled.PauseCircleFilled
                    false -> Icons.Filled.PlayCircleFilled
                },
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(16.dp))
        IconButton(
            onClick = musicPlayer::seekToNext,
            enabled = musicPlayer.hasNextMediaItem(),
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
            onClick = musicPlayer::toggleRepeatMode,
            modifier = Modifier
                .size(20.dp)
                .weight(2f)
        ) {
            Icon(
                imageVector = when (playerState.repeatMode) {
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
internal fun PlayerNowPlayingInfo(
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
