package tech.rk585.vivace.ui.nowPlaying.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamType
import tech.rk585.vivace.ui.common.compose.components.CoverImage
import tech.rk585.vivace.ui.common.compose.theme.translucentSurface
import tech.rk585.vivace.ui.nowPlaying.LocalPlayerViewModel
import kotlin.math.roundToInt

@Composable
fun PlayerBottomBar(
    modifier: Modifier = Modifier
) {
    val viewModel = LocalPlayerViewModel.current
    val streamInfo by viewModel.currentPlayingSong.collectAsState()

    AnimatedVisibility(
        visible = streamInfo != null,
        modifier = modifier
    ) {
        streamInfo?.let { PlayerBottomBarContent(it) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerBottomBarContent(
    streamInfo: StreamInfo
) {
    val viewModel = LocalPlayerViewModel.current
    val swipeableState = rememberSwipeableState(0)
    val endAnchor = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.54f) },
                orientation = Orientation.Horizontal
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                viewModel.stopPlayback()
            }
        }

        PlayerBottomBarStatelessContent(
            streamInfo = streamInfo,
            xOffset = swipeableState.offset.value.roundToInt(),
            playPause = when {
                viewModel.songIsPlaying -> Icons.Rounded.PauseCircle
                else -> Icons.Rounded.PlayCircle
            },
            onTogglePlaybackState = viewModel::togglePlaybackState,
            onTap = { viewModel.showFullScreen = true }
        )
    }
}

@Composable
fun PlayerBottomBarStatelessContent(
    streamInfo: StreamInfo,
    xOffset: Int,
    playPause: ImageVector,
    onTogglePlaybackState: () -> Unit,
    onTap: (Offset) -> Unit,
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(xOffset, 0) }
            .height(64.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTap
                )
            }
            .translucentSurface()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoverImage(
                data = streamInfo.thumbnailUrl,
                size = 64.dp,
                shape = RoundedCornerShape(0.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
            ) {
                Text(
                    text = streamInfo.name,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = streamInfo.uploaderName,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.graphicsLayer {
                        alpha = 0.60f
                    }
                )
            }
            IconButton(
                onClick = onTogglePlaybackState,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = playPause,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayerBottomBarPreview() {
    MaterialTheme {
        PlayerBottomBarStatelessContent(
            streamInfo = StreamInfo(
                0,
                "https://www.youtube.com/watch?v=IdJgdfrCgBg",
                "https://www.youtube.com/watch?v=IdJgdfrCgBg",
                StreamType.AUDIO_STREAM,
                "IdJgdfrCgBg",
                "Somebdy like you",
                18
            ),
            xOffset = 0,
            playPause = Icons.Rounded.PlayCircle,
            onTogglePlaybackState = { /*TODO*/ },
            onTap = { }
        )
    }
}
