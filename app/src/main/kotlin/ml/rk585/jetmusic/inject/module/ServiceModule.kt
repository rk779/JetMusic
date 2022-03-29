package ml.rk585.jetmusic.inject.module

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import ml.rk585.jetmusic.core.media.util.JetMusicResolver

@InstallIn(ServiceComponent::class)
@Module
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
    fun provideResolvingMediaSourceFactory(
        @ApplicationContext context: Context,
        jetMusicResolver: JetMusicResolver
    ): ResolvingDataSource.Factory {
        return ResolvingDataSource.Factory(
            DefaultDataSource.Factory(context),
            jetMusicResolver
        )
    }

    @Provides
    @ServiceScoped
    fun provideDefaultMediaSourceFactory(
        resolvingDataSourceFactory: ResolvingDataSource.Factory
    ): DefaultMediaSourceFactory {
        return DefaultMediaSourceFactory(resolvingDataSourceFactory)
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
