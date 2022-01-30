package ml.rk585.jetmusic.inject.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import ml.rk585.jetmusic.data.service.PlayerConnection
import ml.rk585.jetmusic.data.service.PlayerConnectionImpl
import ml.rk585.jetmusic.util.CoroutineDispatchers
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

    @Provides
    fun providePlayerConnection(
        @ApplicationContext context: Context
    ): PlayerConnection {
        return PlayerConnectionImpl(
            context = context
        )
    }
}
