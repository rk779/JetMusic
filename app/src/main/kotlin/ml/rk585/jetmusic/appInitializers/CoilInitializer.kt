package ml.rk585.jetmusic.appInitializers

import android.app.Application
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import dagger.hilt.android.qualifiers.ApplicationContext
import ml.rk585.jetmusic.core.base.util.CoroutineDispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import javax.inject.Inject

class CoilInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
    private val okHttpClient: OkHttpClient
) : AppInitializer {

    override fun init(application: Application) {
        // Don't limit concurrent network requests by host.
        val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

        val coilOkHttpClient = okHttpClient.newBuilder()
            .dispatcher(dispatcher)
            .build()

        val diskCache = DiskCache.Builder()
            .directory(context.cacheDir.resolve("image_cache"))
            .maxSizeBytes(25L * 1024 * 1024) // 25MB
            .build()

        Coil.setImageLoader {
            ImageLoader.Builder(application)
                .diskCache(diskCache)
                .okHttpClient(coilOkHttpClient)
                .dispatcher(dispatchers.io)
                .build()
        }
    }
}
