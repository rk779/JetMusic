package tech.rk585.vivace.appinitializers

import android.app.Application
import cf.rk585.vivace.core.base.appinitializer.AppInitializer
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) {
    fun init(application: Application) {
        initializers.forEach { initializer ->
            initializer.init(application)
        }
    }
}
