package ml.rk585.jetmusic.core.base.extensions

fun String?.orNA() = when (this.isNullOrEmpty()) {
    false -> this
    else -> "N/A"
}

fun String.stripAlbumPrefix(): String {
    return this.removePrefix("Album")
        .trim()
        .removePrefix("–")
        .trim()
}
