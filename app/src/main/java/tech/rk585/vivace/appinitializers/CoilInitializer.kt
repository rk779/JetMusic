package tech.rk585.vivace.appinitializers

import android.app.Application
import android.content.Context
import cf.rk585.vivace.core.base.appinitializer.AppInitializer
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(ExperimentalCoilApi::class)
class CoilInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient
) : AppInitializer {

    override fun init(application: Application) {
        // Don't limit concurrent network requests by host.
        val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }
        val coilOkHttpClient = okHttpClient.newBuilder()
            .dispatcher(dispatcher)
            .build()

        val diskCache = DiskCache.Builder(context)
            .directory(context.filesDir.resolve("image_cache"))
            .maxSizeBytes(25L * 1024 * 1024) // 25MB
            .build()

        Coil.setImageLoader {
            ImageLoader.Builder(application)
                .diskCache(diskCache)
                .okHttpClient(coilOkHttpClient)
                // Show a short crossFade when loading images asynchronously.
                .crossfade(true)
                // Ignore the network cache headers and always read from/write to the disk cache.
                .respectCacheHeaders(false)
                .build()
        }
    }
}
