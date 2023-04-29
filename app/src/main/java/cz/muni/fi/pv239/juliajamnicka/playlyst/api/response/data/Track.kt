package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val uri: String,
    val is_local: Boolean
)
