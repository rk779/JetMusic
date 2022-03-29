package ml.rk585.jetmusic.core.data.repositories.artist

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.schabi.newpipe.extractor.channel.ChannelInfo
import javax.inject.Singleton

typealias ArtistStore = Store<String, ChannelInfo>

@InstallIn(SingletonComponent::class)
@Module
object ArtistModule {

    @Singleton
    @Provides
    fun provideArtistStore(
        artistDataSource: ArtistDataSource
    ): ArtistStore {
        return StoreBuilder.from(
            fetcher = Fetcher.of { id: String ->
                artistDataSource(id)
            }
        ).build()
    }
}
