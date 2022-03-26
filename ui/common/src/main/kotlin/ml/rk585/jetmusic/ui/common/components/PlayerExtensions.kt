package ml.rk585.jetmusic.ui.common.components

import android.content.ComponentName
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun rememberMediaSessionPlayer(clazz: Class<out Any>): State<MediaController?> {

    val context = LocalContext.current
    val mediaController = remember { mutableStateOf<MediaController?>(null) }

    DisposableEffect(Unit) {
        val mediaControllerFuture = MediaController.Builder(
            context,
            SessionToken(
                context,
                ComponentName(
                    context, clazz
                )
            )
        ).buildAsync()

        mediaControllerFuture.addListener({
            mediaController.value = mediaControllerFuture.get()
        }, MoreExecutors.directExecutor())

        onDispose {
            MediaController.releaseFuture(mediaControllerFuture)
        }
    }

    return mediaController
}

@Composable
fun rememberCurrentMediaItem(player: Player): MediaItem {

    var currentMediaItem by remember(player) {
        mutableStateOf(player.currentMediaItem)
    }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentMediaItem = mediaItem
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    return currentMediaItem ?: MediaItem.EMPTY
}

@Composable
fun rememberPlayProgress(player: Player): State<Pair<Long, Long>?> {
    return produceState(
        initialValue = player.currentPosition to player.duration,
        key1 = player
    ) {
        while (isActive) {
            value = if (player.currentMediaItem == null) {
                0L to 1L
            } else {
                player.currentPosition to player.duration.coerceAtLeast(1)
            }
            delay(500)
        }
    }
}
