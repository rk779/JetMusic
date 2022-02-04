package ml.rk585.jetmusic.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.toEncodedUri(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

fun String.toDecodedUri(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}
