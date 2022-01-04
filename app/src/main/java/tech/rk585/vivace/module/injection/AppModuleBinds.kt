package tech.rk585.vivace.module.injection

import cf.rk585.vivace.core.base.appinitializer.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import tech.rk585.vivace.appinitializers.CoilInitializer
import tech.rk585.vivace.appinitializers.NewPipeInitializer

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideNewPipeInitializer(bind: NewPipeInitializer): AppInitializer
}
