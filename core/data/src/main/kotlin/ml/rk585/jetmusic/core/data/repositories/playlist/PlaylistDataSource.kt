package ml.rk585.jetmusic.core.data.repositories.playlist

import kotlinx.coroutines.withContext
import ml.rk585.jetmusic.core.base.util.CoroutineDispatchers
import ml.rk585.jetmusic.core.base.util.ExtractorHelper
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import javax.inject.Inject

class PlaylistDataSource @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val extractorHelper: ExtractorHelper
) {
    suspend operator fun invoke(id: String): PlaylistInfo {
        return withContext(dispatchers.network) {
            extractorHelper.getPlaylist(id)
        }
    }
}
