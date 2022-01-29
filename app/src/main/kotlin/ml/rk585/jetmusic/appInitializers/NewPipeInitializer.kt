package ml.rk585.jetmusic.appInitializers

import android.app.Application
import ml.rk585.jetmusic.util.CustomDownloader
import org.schabi.newpipe.extractor.NewPipe
import javax.inject.Inject

class NewPipeInitializer @Inject constructor(
    private val customDownloader: CustomDownloader
) : AppInitializer {

    override fun init(application: Application) {
        NewPipe.init(customDownloader)
    }
}
