package cf.rk585.vivace.core.base.extensions

import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun timeAddZeros(number: Int?, ifZero: String = ""): String {
    return when (number) {
        0 -> ifZero
        in 1..9 -> "0$number"
        else -> number.toString()
    }
}

fun Long.secondsToDuration(): String {
    return this.seconds.toString(DurationUnit.MINUTES, 2)
        .removeSuffix("m")
}
