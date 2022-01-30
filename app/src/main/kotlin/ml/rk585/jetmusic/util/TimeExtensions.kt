package ml.rk585.jetmusic.util

fun Long.formatAsPlayerTime(): String {
    val minutes = String.format("%02d", this / 60_000L)
    val seconds = String.format("%02d", (this % 60_000L) / 1000L)
    return "$minutes:$seconds"
}
