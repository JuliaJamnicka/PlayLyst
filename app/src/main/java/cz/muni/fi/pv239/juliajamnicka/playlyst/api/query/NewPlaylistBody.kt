package cz.muni.fi.pv239.juliajamnicka.playlyst.api.query

data class NewPlaylistBody(
    val name: String,
    val public: Boolean? = null,
    val collaborative : Boolean? = false,
    val description: String? = null
)
