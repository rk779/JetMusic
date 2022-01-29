package ml.rk585.jetmusic.data.model

data class SearchQuery(
    val query: String = "",
    val type: SearchType = SearchType.MUSIC
)

enum class SearchType {
    ALBUMS, ARTISTS, MUSIC, PLAYLISTS
}
