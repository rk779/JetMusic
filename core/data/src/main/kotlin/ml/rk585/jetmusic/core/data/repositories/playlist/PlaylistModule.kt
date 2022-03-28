package ml.rk585.jetmusic.core.data.repositories.playlist

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import javax.inject.Singleton

typealias PlaylistStore = Store<String, PlaylistInfo>

@InstallIn(SingletonComponent::class)
@Module
object PlaylistModule {

    @Singleton
    @Provides
    fun providePlaylistStore(
        playlistDataSource: PlaylistDataSource
    ): PlaylistStore {
        return StoreBuilder.from(
            fetcher = Fetcher.of { id: String ->
                playlistDataSource(id)
            }
        ).build()
    }
}
