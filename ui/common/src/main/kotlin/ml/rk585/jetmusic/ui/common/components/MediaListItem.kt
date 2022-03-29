package ml.rk585.jetmusic.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ml.rk585.jetmusic.core.base.extensions.toCompactView
import ml.rk585.jetmusic.core.base.util.Extractor
import ml.rk585.jetmusic.ui.common.LocalMusicPlayer
import ml.rk585.jetmusic.ui.common.R
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@Composable
fun MediaListItem(
    item: InfoItem,
    modifier: Modifier = Modifier,
    onClickArtist: ((String) -> Unit)? = null,
    onClickPlaylist: ((String) -> Unit)? = null
) {
    val musicPlayer = LocalMusicPlayer.current

    Row(
        modifier = modifier
            .clickable {
                when (item.infoType) {
                    InfoItem.InfoType.CHANNEL -> {
                        Extractor
                            .extractChannelId((item as ChannelInfoItem).url)
                            ?.let { channelId ->
                                onClickArtist?.invoke(channelId)
                            }
                    }
                    InfoItem.InfoType.PLAYLIST -> {
                        Extractor
                            .extractPlaylistId((item as PlaylistInfoItem).url)
                            ?.let { playlistId ->
                                onClickPlaylist?.invoke(playlistId)
                            }
                    }
                    InfoItem.InfoType.STREAM -> musicPlayer.play(item as StreamInfoItem)
                    else -> Unit
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JetImage(
            data = item.thumbnailUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            size = 48.dp,
            shape = if (item.infoType == InfoItem.InfoType.CHANNEL) CircleShape else RectangleShape
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = when (item.infoType) {
                    InfoItem.InfoType.STREAM -> (item as StreamInfoItem).uploaderName
                    InfoItem.InfoType.PLAYLIST -> (item as PlaylistInfoItem).uploaderName +
                            if (item.streamCount > 0) " • ${item.streamCount} songs" else ""
                    InfoItem.InfoType.CHANNEL -> stringResource(id = R.string.artist) +
                            if ((item as ChannelInfoItem).subscriberCount > 0) " • ${item.subscriberCount.toCompactView()} subscribers" else ""
                    else -> stringResource(id = R.string.unknown)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
