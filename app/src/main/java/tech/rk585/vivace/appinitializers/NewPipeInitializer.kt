package tech.rk585.vivace.appinitializers

import android.app.Application
import cf.rk585.vivace.core.base.appinitializer.AppInitializer
import cf.rk585.vivace.core.data.util.CustomDownloader
import org.schabi.newpipe.extractor.NewPipe
import javax.inject.Inject

class NewPipeInitializer @Inject constructor(
    private val customDownloader: CustomDownloader
): AppInitializer {

    override fun init(application: Application) {
        NewPipe.init(customDownloader)
    }
}
