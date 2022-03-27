package ml.rk585.jetmusic.inject.module

import android.content.Context
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import ml.rk585.jetmusic.core.base.util.CoroutineDispatchers
import ml.rk585.jetmusic.core.base.util.Downloader
import ml.rk585.jetmusic.core.base.util.ExtractorHelper
import ml.rk585.jetmusic.data.service.PlayerConnection
import ml.rk585.jetmusic.data.service.PlayerConnectionImpl
import okhttp3.OkHttpClient
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.services.youtube.YoutubeService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main,
            network = Dispatchers.Default
        )
    }

    @Singleton
    @Provides
    fun provideDownloader(
        @ApplicationContext context: Context,
        okHttpClient: Lazy<OkHttpClient>
    ): Downloader {
        return Downloader(
            context = context,
            okHttpClient = okHttpClient.get()
        )
    }

    @Singleton
    @Provides
    fun provideExtractorHelper(
        dispatchers: CoroutineDispatchers,
        youtubeService: YoutubeService
    ): ExtractorHelper {
        return ExtractorHelper(
            dispatchers = dispatchers,
            youtubeService = youtubeService
        )
    }

    @Provides
    fun providePlayerConnection(
        @ApplicationContext context: Context
    ): PlayerConnection {
        return PlayerConnectionImpl(
            context = context
        )
    }

    @Singleton
    @Provides
    fun provideYoutubeService(): YoutubeService {
        return ServiceList.YouTube
    }
}
