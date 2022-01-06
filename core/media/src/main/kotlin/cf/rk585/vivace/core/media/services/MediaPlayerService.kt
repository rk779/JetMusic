package cf.rk585.vivace.core.media.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import cf.rk585.vivace.core.media.player.AppMediaSource
import cf.rk585.vivace.core.media.player.MediaPlaybackPreparer
import cf.rk585.vivace.core.media.player.MediaPlayerNotificationListener
import cf.rk585.vivace.core.media.player.MediaPlayerNotificationManager
import cf.rk585.vivace.core.media.player.MediaPlayerQueueNavigator
import cf.rk585.vivace.core.media.services.MediaPlayerServiceConnection.Companion.REFRESH_MEDIA_BROWSER_CHILDREN
import cf.rk585.vivace.core.media.services.MediaPlayerServiceConnection.Companion.START_MEDIA_PLAYBACK_ACTION
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: OkHttpDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaSource: AppMediaSource

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var mediaPlayerNotificationManager: MediaPlayerNotificationManager

    private var currentPlayingMedia: MediaMetadataCompat? = null

    private var isPlayerInitialized = false

    var isForegroundService: Boolean = false

    companion object {
        private const val TAG = "MediaPlayerService"
        const val MEDIA_ROOT_ID = "TUVESUFfUk9PVF9JRA"
        const val ACTION_NOTIFICATION_CLICK = "ACTION_NOTIFICATION_CLICK"

        var currentDuration: Long = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()

        val intent = Intent()
        intent.setClassName(this, "")
        intent.apply {
            action = ACTION_NOTIFICATION_CLICK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        mediaSession = MediaSessionCompat(this, TAG)
            .apply {
                setSessionActivity(pendingIntent)
                isActive = true
            }

        val mediaPlaybackPreparer = MediaPlaybackPreparer(mediaSource) { mediaMetadata ->
            currentPlayingMedia = mediaMetadata
            preparePlayer(mediaSource.mediaMetaDataSongs, mediaMetadata, true)
        }
        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlaybackPreparer(mediaPlaybackPreparer)
            setQueueNavigator(MediaPlayerQueueNavigator(mediaSession, mediaSource))
            setPlayer(exoPlayer)
        }

        this.sessionToken = mediaSession.sessionToken

        mediaPlayerNotificationManager = MediaPlayerNotificationManager(
            this,
            mediaSession.sessionToken,
            MediaPlayerNotificationListener(this)
        ) {
            currentDuration = exoPlayer.duration
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when (action) {
            START_MEDIA_PLAYBACK_ACTION -> {
                mediaPlayerNotificationManager.showNotification(exoPlayer)
            }
            REFRESH_MEDIA_BROWSER_CHILDREN -> {
                mediaSource.refresh()
                notifyChildrenChanged(MEDIA_ROOT_ID)
            }
            else -> Unit
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = mediaSource.whenReady { isInitialized ->
                    if (isInitialized) {

                        result.sendResult(mediaSource.asMediaItems())
                        if (!isPlayerInitialized && mediaSource.mediaMetaDataSongs.isNotEmpty()) {
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
            else -> Unit
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.release()
    }

    private fun preparePlayer(
        mediaMetaData: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean
    ) {
        val indexToPlay = if (currentPlayingMedia == null) 0 else mediaMetaData.indexOf(itemToPlay)
        exoPlayer.setMediaSource(mediaSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(indexToPlay, 0L)
        exoPlayer.playWhenReady = playWhenReady
    }
}
