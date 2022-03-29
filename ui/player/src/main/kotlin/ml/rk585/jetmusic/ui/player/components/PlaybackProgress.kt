package ml.rk585.jetmusic.ui.player.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import ml.rk585.jetmusic.core.base.extensions.millisToDuration
import ml.rk585.jetmusic.core.media.MusicPlayer
import ml.rk585.jetmusic.core.media.model.PlaybackProgress
import ml.rk585.jetmusic.ui.common.LocalMusicPlayer
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle
import kotlin.math.roundToLong

@Composable
internal fun PlaybackProgress(
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    musicPlayer: MusicPlayer = LocalMusicPlayer.current
) {
    val playbackProgress by rememberStateWithLifecycle(musicPlayer.playbackProgress)
    val playbackState by rememberStateWithLifecycle(musicPlayer.playbackState)
    val (draggingProgress, setDraggingProgress) = remember { mutableStateOf<Float?>(null) }

    Box(modifier = modifier) {
        PlaybackProgressSlider(
            draggingProgress = draggingProgress,
            setDraggingProgress = setDraggingProgress,
            playbackProgress = playbackProgress,
            playbackState = playbackState,
            contentColor = contentColor,
            onSeekTo = musicPlayer::seekTo
        )
        PlaybackProgressDuration(
            playbackProgress = playbackProgress,
            draggingProgress = draggingProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
internal fun PlaybackProgressSlider(
    draggingProgress: Float?,
    setDraggingProgress: (Float?) -> Unit,
    modifier: Modifier = Modifier,
    playbackProgress: PlaybackProgress,
    playbackState: Int,
    contentColor: Color = LocalContentColor.current,
    bufferedProgressColor: Color = contentColor.copy(alpha = 0.25f),
    onSeekTo: (Long) -> Unit
) {
    val updatedPlaybackProgress by rememberUpdatedState(playbackProgress)
    val updatedDraggingProgress by rememberUpdatedState(draggingProgress)
    val bufferedProgress by animatePlaybackProgress(playbackProgress.bufferedProgress)
    val isBuffering = playbackState == Player.STATE_BUFFERING

    val sliderColors = SliderDefaults.colors(
        thumbColor = contentColor,
        activeTrackColor = contentColor,
        inactiveTrackColor = contentColor.copy(alpha = ContentAlpha.disabled)
    )
    val linearProgressMod = Modifier
        .fillMaxWidth(0.95f) // reduce linearProgressIndicators width to match Slider's
        .clip(CircleShape) // because Slider is rounded

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (!isBuffering) {
            LinearProgressIndicator(
                progress = bufferedProgress,
                color = bufferedProgressColor,
                trackColor = Color.Transparent,
                modifier = linearProgressMod
            )
        }

        Slider(
            value = draggingProgress ?: playbackProgress.progress,
            onValueChange = { value ->
                if (!isBuffering) setDraggingProgress(value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha = if (isBuffering) 0f else 1f),
            colors = sliderColors,
            onValueChangeFinished = {
                onSeekTo(
                    (updatedPlaybackProgress.total.toFloat() * (updatedDraggingProgress
                        ?: 0f)).roundToLong()
                )
                setDraggingProgress(null)
            }
        )

        if (isBuffering) {
            LinearProgressIndicator(
                color = contentColor,
                modifier = linearProgressMod
            )
        }
    }
}

@Composable
internal fun PlaybackProgressDuration(
    playbackProgress: PlaybackProgress,
    draggingProgress: Float?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val currentDuration = when {
            draggingProgress != null -> (playbackProgress.total.toFloat() * draggingProgress).toLong()
                .millisToDuration()
            else -> playbackProgress.currentDuration
        }

        Text(
            text = currentDuration,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = playbackProgress.totalDuration,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
internal fun animatePlaybackProgress(
    targetValue: Float
): State<Float> {
    return animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )
}
