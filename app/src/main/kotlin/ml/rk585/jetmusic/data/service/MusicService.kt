package ml.rk585.jetmusic.data.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import dagger.hilt.android.AndroidEntryPoint
import ml.rk585.jetmusic.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaLibraryService() {

    @Inject
    lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, MainActivity::class.java)
        val immutableFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaLibrarySession.Builder(this, exoPlayer, librarySessionCallback)
            .setMediaItemFiller(CustomMediaFiller())
            .setSessionActivity(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        exoPlayer.release()
        mediaSession.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession {
        return mediaSession
    }

    private val librarySessionCallback = object : MediaLibrarySession.MediaLibrarySessionCallback {

    }

    private class CustomMediaFiller : MediaSession.MediaItemFiller {
        override fun fillInLocalConfiguration(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItem: MediaItem
        ): MediaItem {
            return mediaItem.buildUpon()
                .setMediaId(mediaItem.mediaId)
                .setUri(mediaItem.mediaMetadata.mediaUri)
                .setMediaMetadata(mediaItem.mediaMetadata)
                .build()
        }
    }
}
