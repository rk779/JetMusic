package ml.rk585.jetmusic.core.data.repositories.artist

import kotlinx.coroutines.withContext
import ml.rk585.jetmusic.core.base.util.CoroutineDispatchers
import ml.rk585.jetmusic.core.base.util.ExtractorHelper
import org.schabi.newpipe.extractor.channel.ChannelInfo
import javax.inject.Inject

class ArtistDataSource @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val extractorHelper: ExtractorHelper
) {
    suspend operator fun invoke(id: String): ChannelInfo {
        return withContext(dispatchers.network) {
            extractorHelper.getChannel(id)
        }
    }
}
