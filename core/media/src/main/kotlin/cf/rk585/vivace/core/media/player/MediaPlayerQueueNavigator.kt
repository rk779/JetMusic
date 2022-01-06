package cf.rk585.vivace.core.media.player

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator

class MediaPlayerQueueNavigator(
    mediaSession: MediaSessionCompat,
    private val mediaSource: AppMediaSource
) : TimelineQueueNavigator(mediaSession) {

    override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
        return mediaSource.mediaMetaDataSongs[windowIndex].description
    }
}
