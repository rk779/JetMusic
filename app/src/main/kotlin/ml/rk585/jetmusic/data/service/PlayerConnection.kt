package ml.rk585.jetmusic.data.service

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope

interface PlayerConnection {
    fun play(mediaItem: MediaItem)
    fun play(mediaItems: List<MediaItem>)
    fun releaseMediaController()
}

class PlayerConnectionImpl(
    private val context: Context,
    coroutineScope: CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope,
) : PlayerConnection, CoroutineScope by coroutineScope {

    private lateinit var mediaControllerFuture: ListenableFuture<MediaController>
    private lateinit var mediaController: MediaController

    init {
        initializeController()
    }

    override fun play(mediaItem: MediaItem) {
        mediaController.setMediaItem(mediaItem)
        mediaController.prepare()
        mediaController.play()
    }

    override fun play(mediaItems: List<MediaItem>) {
        mediaController.setMediaItems(mediaItems)
        mediaController.prepare()
        mediaController.play()
    }

    private fun initializeController() {
        mediaControllerFuture = MediaController.Builder(
            context,
            SessionToken(
                context,
                ComponentName(context, MusicService::class.java)
            )
        ).buildAsync()

        mediaControllerFuture.addListener({
            mediaController = mediaControllerFuture.get()
        }, MoreExecutors.directExecutor())
    }

    override fun releaseMediaController() {
        MediaController.releaseFuture(mediaControllerFuture)
    }
}