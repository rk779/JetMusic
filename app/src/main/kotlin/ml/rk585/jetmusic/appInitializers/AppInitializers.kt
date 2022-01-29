package ml.rk585.jetmusic.appInitializers

import android.app.Application
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) {
    fun init(application: Application) {
        initializers.forEach { appInitializer ->
            appInitializer.init(application)
        }
    }
}
