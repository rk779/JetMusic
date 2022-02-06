package ml.rk585.jetmusic.data.model

import org.schabi.newpipe.extractor.stream.StreamInfoItem

data class StreamsCountDuration(
    val count: Int = 0,
    val duration: Long = Long.MAX_VALUE
) {
    companion object {
        fun from(items: List<StreamInfoItem>): StreamsCountDuration {
            return StreamsCountDuration(
                count = items.size,
                duration = items.sumOf { it.duration }
            )
        }
    }
}
