package ml.rk585.jetmusic.inject.module

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    @Provides
    @ServiceScoped
    fun provideOkhttpDataSourceFactory(
        @ApplicationContext context: Context,
        okHttpClient: Lazy<OkHttpClient>
    ): OkHttpDataSource.Factory {
        return OkHttpDataSource.Factory(okHttpClient.get())
            .setUserAgent(Util.getUserAgent(context, context.packageName))
    }

    @Provides
    @ServiceScoped
    fun provideDefaultDataSourceFactory(
        @ApplicationContext context: Context,
        okHttpDataSourceFactory: OkHttpDataSource.Factory
    ): DefaultDataSource.Factory {
        return DefaultDataSource.Factory(
            context, okHttpDataSourceFactory
        )
    }

    @Provides
    @ServiceScoped
    fun provideDefaultMediaSourceFactory(
        defaultDataSourceFactory: DefaultDataSource.Factory
    ): DefaultMediaSourceFactory {
        return DefaultMediaSourceFactory(defaultDataSourceFactory)
    }

    @Provides
    @ServiceScoped
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
        defaultMediaSourceFactory: DefaultMediaSourceFactory
    ): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .setMediaSourceFactory(defaultMediaSourceFactory)
            .build()
    }
}
