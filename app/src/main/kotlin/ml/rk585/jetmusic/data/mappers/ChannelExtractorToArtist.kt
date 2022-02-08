package ml.rk585.jetmusic.data.mappers

import ml.rk585.jetmusic.data.model.Artist
import org.schabi.newpipe.extractor.channel.ChannelExtractor
import org.schabi.newpipe.extractor.feed.FeedExtractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelExtractorToArtist @Inject constructor() :
    Mapper<Pair<ChannelExtractor, FeedExtractor>, Artist> {

    override suspend fun map(from: Pair<ChannelExtractor, FeedExtractor>): Artist {
        return Artist(
            name = from.first.name,
            isVerified = from.first.isVerified,
            avatarUrl = from.first.avatarUrl,
            description = from.first.description,
            items = from.second.initialPage.items ?: emptyList()
        )
    }
}