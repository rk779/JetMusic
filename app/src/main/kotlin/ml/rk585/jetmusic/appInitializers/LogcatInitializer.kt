package ml.rk585.jetmusic.appInitializers

import android.app.Application
import logcat.AndroidLogcatLogger
import ml.rk585.jetmusic.core.base.appInitializer.AppInitializer
import javax.inject.Inject

class LogcatInitializer @Inject constructor() : AppInitializer {

    override fun init(application: Application) {
        AndroidLogcatLogger.installOnDebuggableApp(application)
    }
}
