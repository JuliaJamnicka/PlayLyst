package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data

data class Album(
    val album_type: String,
    val total_tracks: Int,
    val available_markets: List<String>,
    val href: String,
    val images: List<Image>,
    val name: String,
    val genres: List<String>
)
