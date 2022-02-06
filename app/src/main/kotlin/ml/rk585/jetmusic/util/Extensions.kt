package ml.rk585.jetmusic.util

import kotlin.math.max

infix fun Float.muteUntil(that: Float): Float {
    return max(this - that, 0.0f) * (1 / (1 - that))
}

