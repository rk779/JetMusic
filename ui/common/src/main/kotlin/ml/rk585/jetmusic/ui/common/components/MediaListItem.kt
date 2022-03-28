package ml.rk585.jetmusic.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ml.rk585.jetmusic.ui.common.R
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaListItem(
    item: InfoItem,
    modifier: Modifier = Modifier,
    onClickArtist: (ChannelInfoItem) -> Unit,
    onClickPlaylist: (PlaylistInfoItem) -> Unit,
    onClickSong: (StreamInfoItem) -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable {
                when (item.infoType) {
                    InfoItem.InfoType.CHANNEL -> onClickArtist(item as ChannelInfoItem)
                    InfoItem.InfoType.PLAYLIST -> onClickPlaylist(item as PlaylistInfoItem)
                    InfoItem.InfoType.STREAM -> onClickSong(item as StreamInfoItem)
                    else -> Unit
                }
            }
            .then(modifier),
        icon = {
            JetImage(
                data = item.thumbnailUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                size = 48.dp,
                shape = if (item.infoType == InfoItem.InfoType.CHANNEL) CircleShape else RectangleShape
            )
        },
        secondaryText = {
            Text(
                text = when (item.infoType) {
                    InfoItem.InfoType.STREAM -> (item as StreamInfoItem).uploaderName
                    InfoItem.InfoType.PLAYLIST -> "${(item as PlaylistInfoItem).uploaderName} • ${item.streamCount} songs"
                    InfoItem.InfoType.CHANNEL -> stringResource(id = R.string.artist)
                    else -> stringResource(id = R.string.unknown)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLineSecondaryText = true,
        text = {
            Text(
                text = item.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}
