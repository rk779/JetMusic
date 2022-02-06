package ml.rk585.jetmusic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ml.rk585.jetmusic.util.muteUntil

object ScaledCoverImageDefaults {
    val height = 300.dp
}

@Composable
fun scaledCoverImageScrollProgress(
    listState: LazyListState,
    height: Dp = ScaledCoverImageDefaults.height
): State<Float> {
    val density = LocalDensity.current.density
    val headerProgress = remember { mutableStateOf(0f) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .map { (listState.firstVisibleItemScrollOffset / density).dp / height }
            .map { if (listState.firstVisibleItemIndex == 0) it else 1f }
            .map { it.coerceIn(0f, 1f) }
            .distinctUntilChanged()
            .collectLatest { headerProgress.value = it }
    }

    return headerProgress
}

@Composable
fun ScaledCoverImage(
    modifier: Modifier = Modifier,
    imageData: Any? = null,
    icon: VectorPainter? = null,
    height: Dp = ScaledCoverImageDefaults.height,
    imageHeightFraction: Float = 0.85f,
    offsetProgress: Float = 0f
) {
    // scale down as the header is scrolled away
    val imageAlpha = 1 - (offsetProgress.muteUntil(0.5f) * 2f)
    val imageScale = 1 - offsetProgress.coerceAtMost(0.5f)
    val imageHeight = height * imageHeightFraction
    val scaledImageHeight = imageHeight * imageScale
    val imageTopPadding = imageHeight * (1 - imageScale)

    Column(
        modifier = modifier
    ) {
        JetImage(
            data = imageData,
            shape = RectangleShape,
            tonalElevation = 10.dp,
            backgroundIcon = icon ?: rememberVectorPainter(Icons.Default.MusicNote),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = imageTopPadding)
                .height(scaledImageHeight)
                .alpha(imageAlpha),
            contentScale = ContentScale.Crop
        )
    }
}
