package tech.rk585.vivace.ui.nowPlaying.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults.IndicatorBackgroundOpacity
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamType
import tech.rk585.vivace.ui.common.compose.components.CoverImage
import tech.rk585.vivace.ui.common.compose.utils.ADAPTIVE_COLOR_ANIMATION
import tech.rk585.vivace.ui.common.compose.utils.adaptiveColor
import tech.rk585.vivace.ui.nowPlaying.LocalPlayerViewModel
import kotlin.math.roundToInt

@Composable
fun PlayerScreen(backPressedDispatcher: OnBackPressedDispatcher) {
    val viewModel = LocalPlayerViewModel.current
    val streamInfo by viewModel.currentPlayingSong.collectAsState()

    AnimatedVisibility(
        visible = streamInfo != null && viewModel.showFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )
    ) {
        streamInfo?.let { PlayerScreen(it, backPressedDispatcher) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PlayerScreen(
    streamInfo: StreamInfo,
    backPressedDispatcher: OnBackPressedDispatcher
) {
    val viewModel = LocalPlayerViewModel.current
    val swipeableState = rememberSwipeableState(0)
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.showFullScreen = false
            }
        }
    }

    var sliderIsChanging by remember { mutableStateOf(false) }

    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress = if (sliderIsChanging) localSliderValue else viewModel.currentSongProgress

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.34f) },
                orientation = Orientation.Vertical
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                viewModel.showFullScreen = false
            }
        }

        PlayerScreenStatelessContent(
            streamInfo = streamInfo,
            yOffset = swipeableState.offset.value.roundToInt(),
            playPauseIcon = when {
                viewModel.songIsPlaying -> Icons.Rounded.PauseCircle
                else -> Icons.Rounded.PlayCircle
            },
            playbackProgress = sliderProgress,
            currentTime = viewModel.currentPlaybackFormattedPosition,
            totalTime = viewModel.currentSongFormattedDuration,
            onRewind = viewModel::rewind,
            onForward = viewModel::fastForward,
            onTogglePlayback = viewModel::togglePlaybackState,
            onSliderChange = { position ->
                localSliderValue = position
                sliderIsChanging = true
            },
            onSliderChangeFinished = {
                viewModel.seekToFraction(localSliderValue)
                sliderIsChanging = false
            },
            onClose = { viewModel.showFullScreen = false }
        )

        LaunchedEffect("playbackPosition") {
            viewModel.updateCurrentPlaybackPosition()
        }

        DisposableEffect(backPressedDispatcher) {
            backPressedDispatcher.addCallback(backCallback)

            onDispose {
                backCallback.remove()
                viewModel.showFullScreen = false
            }
        }
    }
}

@Composable
fun PlayerScreenStatelessContent(
    streamInfo: StreamInfo,
    yOffset: Int,
    playPauseIcon: ImageVector,
    playbackProgress: Float,
    currentTime: String,
    totalTime: String,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit,
    onClose: () -> Unit
) {
    val adaptiveColor by adaptiveColor(streamInfo.thumbnailUrl, MaterialTheme.colors.onBackground)
    val contentColor by animateColorAsState(adaptiveColor.color, ADAPTIVE_COLOR_ANIMATION)

    val sliderColors = SliderDefaults.colors(
        thumbColor = contentColor,
        activeTrackColor = contentColor,
        inactiveTrackColor = contentColor.copy(
            alpha = IndicatorBackgroundOpacity
        )
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(0, yOffset) }
            .fillMaxSize()
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .background(adaptiveColor.gradient)
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Column {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "close"
                        )
                    }
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 32.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .weight(1f, fill = false)
                                .aspectRatio(1f)
                                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.08f))
                        ) {
                            CoverImage(
                                data = streamInfo.thumbnailUrl,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Text(
                            text = streamInfo.name,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Text(
                            text = streamInfo.uploaderName,
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.graphicsLayer {
                                alpha = 0.60f
                            }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Slider(
                                value = playbackProgress,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = sliderColors,
                                onValueChange = onSliderChange,
                                onValueChangeFinished = onSliderChangeFinished,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CompositionLocalProvider(
                                    LocalContentAlpha provides ContentAlpha.medium
                                ) {
                                    Text(text = currentTime)
                                    Text(text = totalTime)
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            IconButton(
                                onClick = onRewind,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Replay10,
                                    contentDescription = "replay 10",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = contentColor
                                )
                            }
                            IconButton(
                                onClick = onTogglePlayback,
                                modifier = Modifier.size(80.dp)
                            ) {
                                Icon(
                                    imageVector = playPauseIcon,
                                    contentDescription = "play",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = contentColor
                                )
                            }
                            IconButton(
                                onClick = onForward,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Forward10,
                                    contentDescription = "forward",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = contentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayerPreview() {
    MaterialTheme {
        PlayerScreenStatelessContent(
            streamInfo = StreamInfo(
                0,
                "https://www.youtube.com/watch?v=IdJgdfrCgBg",
                "https://www.youtube.com/watch?v=IdJgdfrCgBg",
                StreamType.AUDIO_STREAM,
                "IdJgdfrCgBg",
                "Somebdy like you",
                18
            ),
            yOffset = 0,
            playPauseIcon = Icons.Rounded.PlayCircle,
            playbackProgress = 0f,
            currentTime = "0:00",
            totalTime = "10:00",
            onRewind = { /*TODO*/ },
            onForward = { /*TODO*/ },
            onTogglePlayback = { /*TODO*/ },
            onSliderChange = { },
            onSliderChangeFinished = { /*TODO*/ },
            onClose = { /*TODO*/ }
        )
    }
}
