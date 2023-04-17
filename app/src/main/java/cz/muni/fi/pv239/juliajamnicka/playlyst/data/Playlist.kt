package cz.muni.fi.pv239.juliajamnicka.playlyst.data

data class Playlist(
    val id: String,
    val uri: String,
    val name: String,
    val imageLink: String?,
    val songs: List<Song>
)
