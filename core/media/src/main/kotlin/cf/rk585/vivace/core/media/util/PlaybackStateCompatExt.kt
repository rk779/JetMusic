package cf.rk585.vivace.core.media.util

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat

inline val PlaybackStateCompat.isPrepared: Boolean
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING ||
            state == PlaybackStateCompat.STATE_PAUSED

inline val PlaybackStateCompat.isPlaying: Boolean
    get() = state == PlaybackStateCompat.STATE_PLAYING || isBuffering

inline val PlaybackStateCompat.isBuffering
    get() = (state == PlaybackStateCompat.STATE_BUFFERING)

inline val PlaybackStateCompat.isStopped: Boolean
    get() = state == PlaybackStateCompat.STATE_NONE ||
            state == PlaybackStateCompat.STATE_ERROR

inline val PlaybackStateCompat.isIdle
    get() = (state == PlaybackStateCompat.STATE_NONE || state == PlaybackStateCompat.STATE_STOPPED)

inline val PlaybackStateCompat.isError: Boolean
    get() = state == PlaybackStateCompat.STATE_ERROR

inline val PlaybackStateCompat.isPlayEnabled: Boolean
    get() = actions and PlaybackStateCompat.ACTION_PLAY != 0L ||
            (actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L &&
                    state == PlaybackStateCompat.STATE_PAUSED)

inline val PlaybackStateCompat.currentPosition: Long
    get() = if (state == PlaybackStateCompat.STATE_PLAYING) {
        val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
        (position + (timeDelta * playbackSpeed)).toLong()
    } else position
