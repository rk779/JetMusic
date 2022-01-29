package ml.rk585.jetmusic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import coil.compose.AsyncImage
import coil.compose.AsyncImageContent
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun JetImage(
    modifier: Modifier = Modifier,
    data: Any? = null,
    contentDescription: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    contentScale: ContentScale = ContentScale.None,
    tonalElevation: Dp = BottomBarElevationDefault,
    backgroundIcon: VectorPainter = rememberVectorPainter(Icons.Default.MusicNote),
    size: Dp = Dp.Unspecified,
    shape: Shape = RectangleShape
) {
    val context = LocalContext.current
    val iconPadding = if (size.isSpecified) size * 0.25f else 24.dp
    val sizeModifier = if (size.isSpecified) Modifier.size(size) else Modifier

    Surface(
        modifier = modifier
            .then(sizeModifier)
            .aspectRatio(1f),
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale
        ) { state ->
            when (state) {
                is AsyncImagePainter.State.Error,
                is AsyncImagePainter.State.Empty,
                is AsyncImagePainter.State.Loading -> {
                    Icon(
                        painter = backgroundIcon,
                        contentDescription = null,
                        tint = contentColor.copy(alpha = ContentAlpha.disabled),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(containerColor)
                            .padding(iconPadding)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .placeholder(
                                visible = state is AsyncImagePainter.State.Loading,
                                shape = shape,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                }
                is AsyncImagePainter.State.Success -> {
                    AsyncImageContent(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
