package tech.rk585.vivace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import tech.rk585.vivace.appinitializers.AppInitializers
import javax.inject.Inject

@HiltAndroidApp
class Vivace : Application() {

    @Inject
    lateinit var appInitializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        appInitializers.init(this)
    }
}
