package ml.rk585.jetmusic.inject.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ml.rk585.jetmusic.core.base.appInitializer.AppInitializer
import ml.rk585.jetmusic.appInitializers.CoilInitializer
import ml.rk585.jetmusic.appInitializers.LogcatInitializer
import ml.rk585.jetmusic.appInitializers.NewPipeInitializer

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideNewPipeInitializer(bind: NewPipeInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideLogcatInitializer(bind: LogcatInitializer): AppInitializer
}
