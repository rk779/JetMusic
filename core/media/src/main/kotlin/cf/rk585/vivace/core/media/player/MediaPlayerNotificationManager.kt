package cf.rk585.vivace.core.media.player

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MediaPlayerNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: () -> Unit
) {
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager =
            createNotificationManger(mediaController, sessionToken, notificationListener)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private fun createNotificationManger(
        mediaController: MediaControllerCompat,
        sessionToken: MediaSessionCompat.Token,
        notificationListener: PlayerNotificationManager.NotificationListener
    ): PlayerNotificationManager {
        return PlayerNotificationManager.Builder(
            context,
            PLAYBACK_NOTIFICATION_ID,
            PLAYBACK_NOTIFICATION_CHANNEL_ID,
        )
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .setNotificationListener(notificationListener)
            .build()
            .apply {
                setMediaSessionToken(sessionToken)
                setUseStopAction(true)
                setUseNextActionInCompactView(true)
                setUsePreviousActionInCompactView(true)
            }
    }

    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentContentTitle(player: Player): CharSequence {
            newSongCallback()
            return mediaController.metadata.description.title.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(mediaController.metadata.description.iconUri)
                .allowHardware(false)
                .build()
            val result = loader.enqueue(request).job
            result.getCompleted().drawable?.let { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                callback.onBitmap(bitmap)
            }
            return null
        }
    }

    companion object {
        const val PLAYBACK_NOTIFICATION_CHANNEL_ID = "UEBWUJBQ0tfTk9USUZJQ0FUSU9OX0NIQU5ORUxfSUQ"
        const val PLAYBACK_NOTIFICATION_ID = 115234045
    }
}
