package ml.rk585.jetmusic.data.mappers

fun interface Mapper<F, T> {
    suspend fun map(from: F): T
}
