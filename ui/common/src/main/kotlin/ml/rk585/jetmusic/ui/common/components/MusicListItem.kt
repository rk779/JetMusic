package ml.rk585.jetmusic.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.JetImage
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicListItem(
    item: StreamInfoItem,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    ListItem(
        text = {
            Text(
                text = item.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        secondaryText = {
            Text(
                text = stringResource(
                    id = R.string.music_artist_name,
                    item.uploaderName
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        },
        icon = {
            JetImage(
                data = item.thumbnailUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                size = 48.dp,
                shape = CircleShape
            )
        },
        modifier = clickableModifier
            .then(modifier)
    )
}
