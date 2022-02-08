package ml.rk585.jetmusic.data.model

import org.schabi.newpipe.extractor.stream.StreamInfoItem

data class Artist(
    val name: String = "",
    val isVerified: Boolean = false,
    val avatarUrl: String = "",
    val description: String? = null,
    val items: List<StreamInfoItem> = emptyList()
)
