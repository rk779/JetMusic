package ml.rk585.jetmusic.core.media.util

import androidx.core.net.toUri
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.ResolvingDataSource
import kotlinx.coroutines.runBlocking
import ml.rk585.jetmusic.core.base.util.CoroutineDispatchers
import ml.rk585.jetmusic.core.base.util.ExtractorHelper
import ml.rk585.jetmusic.core.base.util.StreamHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JetMusicResolver @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val extractorHelper: ExtractorHelper
) : ResolvingDataSource.Resolver {

    override fun resolveDataSpec(dataSpec: DataSpec): DataSpec {
        val mediaId = dataSpec.uri.toString()
        val streamInfo = runBlocking(dispatchers.io) {
            extractorHelper.getStreamInfo(mediaId)
        }
        val audioStream = StreamHelper.getHighestQualityAudioStream(streamInfo.audioStreams)
        val streamUri = audioStream?.url?.toUri()

        return if (streamUri != null) {
            dataSpec.withUri(streamUri)
        } else dataSpec
    }
}
