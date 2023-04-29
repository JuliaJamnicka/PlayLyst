package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data

data class Artist(
    val href: String,
    val id: String,
    val name: String,
    val uri: String,
    val images: List<Image>
)