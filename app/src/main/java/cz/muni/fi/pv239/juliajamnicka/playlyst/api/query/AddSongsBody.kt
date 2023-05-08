package cz.muni.fi.pv239.juliajamnicka.playlyst.api.query

data class AddSongsBody(
    val position: Int? = null,
    val uris: List<String>
)
