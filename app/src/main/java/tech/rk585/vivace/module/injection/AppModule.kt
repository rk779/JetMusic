package tech.rk585.vivace.module.injection

import android.content.Context
import cf.rk585.vivace.core.base.util.CoroutineDispatchers
import cf.rk585.vivace.core.media.player.AppMediaSource
import cf.rk585.vivace.core.media.services.MediaPlayerServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
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
            network = Dispatchers.IO
        )
    }

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: AppMediaSource
    ): MediaPlayerServiceConnection {
        return MediaPlayerServiceConnection(context, mediaSource)
    }
}
