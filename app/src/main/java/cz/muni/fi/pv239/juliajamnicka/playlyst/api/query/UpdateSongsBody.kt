package cz.muni.fi.pv239.juliajamnicka.playlyst.api.query

data class UpdateSongsBody(
    val position: Int? = null,
    val uris: List<String>
)
