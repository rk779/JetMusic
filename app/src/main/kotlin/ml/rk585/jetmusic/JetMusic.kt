package ml.rk585.jetmusic

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ml.rk585.jetmusic.appInitializers.AppInitializers
import javax.inject.Inject

@HiltAndroidApp
class JetMusic : Application() {

    @Inject
    lateinit var appInitializers: AppInitializers

    override fun onCreate() {
        super.onCreate()

        // Init libraries
        appInitializers.init(this)
    }
}
