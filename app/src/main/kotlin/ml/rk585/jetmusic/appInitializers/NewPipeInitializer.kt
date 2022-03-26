package ml.rk585.jetmusic.appInitializers

import android.app.Application
import ml.rk585.jetmusic.core.base.util.Downloader
import org.schabi.newpipe.extractor.NewPipe
import javax.inject.Inject

class NewPipeInitializer @Inject constructor(
    private val customDownloader: Downloader
) : AppInitializer {

    override fun init(application: Application) {
        NewPipe.init(customDownloader)
    }
}
