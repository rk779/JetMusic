package ml.rk585.jetmusic.core.base.extensions

import kotlin.math.max

infix fun Float.muteUntil(that: Float): Float {
    return max(this - that, 0.0f) * (1 / (1 - that))
}

fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}
