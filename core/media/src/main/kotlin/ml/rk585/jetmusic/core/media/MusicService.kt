package ml.rk585.jetmusic.core.media

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaLibraryService() {

    @Inject
    internal lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaLibrarySession: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
    }

    override fun onDestroy() {
        exoPlayer.release()
        mediaLibrarySession.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession {
        return mediaLibrarySession
    }

    private val mediaItemFiller = object : MediaSession.MediaItemFiller {
        override fun fillInLocalConfiguration(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItem: MediaItem
        ): MediaItem {
            return mediaItem.buildUpon()
                .setMediaId(mediaItem.mediaId)
                .setUri(mediaItem.mediaMetadata.mediaUri)
                .setMediaMetadata(mediaItem.mediaMetadata)
                .setTag(mediaItem.localConfiguration?.tag)
                .build()
        }
    }

    private inner class MediaLibrarySessionCallback :
        MediaLibrarySession.MediaLibrarySessionCallback

    private fun initializeSessionAndPlayer() {
        val intent = Intent(this, Class.forName("ml.rk585.jetmusic.ui.MainActivity"))
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntent(intent)

            val immutableFlag = if (Build.VERSION.SDK_INT >= 23) FLAG_IMMUTABLE else 0
            getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
        }

        mediaLibrarySession =
            MediaLibrarySession.Builder(this, exoPlayer, MediaLibrarySessionCallback())
                .setMediaItemFiller(mediaItemFiller)
                .setSessionActivity(pendingIntent)
                .build()
    }
}
