package tech.rk585.vivace.module.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun getBaseBuilder(cache: Cache): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
    }

    @Provides
    @Singleton
    fun okHttpCache(app: Application): Cache {
        return Cache(app.cacheDir, (25 * 1024 * 1024).toLong()) // 25MB
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(
        cache: Cache
    ): OkHttpClient {
        return getBaseBuilder(cache)
            .build()
    }
}
