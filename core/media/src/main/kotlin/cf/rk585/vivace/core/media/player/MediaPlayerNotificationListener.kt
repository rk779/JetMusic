package cf.rk585.vivace.core.media.player

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import cf.rk585.vivace.core.media.player.MediaPlayerNotificationManager.Companion.PLAYBACK_NOTIFICATION_ID
import cf.rk585.vivace.core.media.services.MediaPlayerService
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MediaPlayerNotificationListener(
    private val mediaPlayerService: MediaPlayerService
) : PlayerNotificationManager.NotificationListener {

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        mediaPlayerService.apply {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        mediaPlayerService.apply {
            if (ongoing || !isForegroundService) {
                ContextCompat.startForegroundService(
                    this,
                    Intent(applicationContext, this::class.java)
                )
                startForeground(PLAYBACK_NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }
    }
}