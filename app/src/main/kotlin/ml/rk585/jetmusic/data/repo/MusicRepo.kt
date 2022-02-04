package ml.rk585.jetmusic.data.repo

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.withContext
import logcat.logcat
import ml.rk585.jetmusic.data.model.SearchQuery
import ml.rk585.jetmusic.data.model.SearchType
import ml.rk585.jetmusic.util.CoroutineDispatchers
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.ServiceList.YouTube
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory
import org.schabi.newpipe.extractor.stream.StreamExtractor
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import javax.inject.Inject

class MusicRepo @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun search(searchQuery: SearchQuery): List<InfoItem> {
        return withContext(dispatchers.network) {
            val contentFilter = when (searchQuery.type) {
                SearchType.ALBUMS -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_ALBUMS)
                SearchType.ARTISTS -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_ARTISTS)
                SearchType.MUSIC -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_SONGS)
                SearchType.PLAYLISTS -> listOf(YoutubeSearchQueryHandlerFactory.MUSIC_PLAYLISTS)
            }
            val extractor = YouTube.getSearchExtractor(
                searchQuery.query,
                contentFilter,
                null
            )
            extractor.fetchPage()
            extractor.initialPage.items ?: emptyList()
        }
    }

    suspend fun getPlayList(url: String): List<StreamInfoItem> {
        return withContext(dispatchers.network) {
            val extractor = YouTube.getPlaylistExtractor(url)
            extractor.fetchPage()
            extractor.initialPage.items ?: emptyList()
        }
    }

    suspend fun getMediaItemFromUrl(item: StreamInfoItem): MediaItem {
        return withContext(dispatchers.network) {
            val extractor = YouTube.getStreamExtractor(item.url)
            extractor.fetchPage()
            MediaItem.Builder()
                .setMediaId(extractor.id)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setMediaUri(getBestStreamUri(extractor))
                        .setTitle(extractor.name)
                        .setArtist(extractor.uploaderName)
                        .setArtworkUri(extractor.thumbnailUrl.toUri())
                        .build()
                )
                .build()
        }
    }

    companion object {
        private fun getBestStreamUri(streamExtractor: StreamExtractor?): Uri {
            val streamUri = streamExtractor?.hlsUrl ?: streamExtractor?.dashMpdUrl
            ?: streamExtractor!!.audioStreams.run { this[this.size - 1] }.url
            logcat { streamUri }
            return streamUri.toUri()
        }
    }
}
