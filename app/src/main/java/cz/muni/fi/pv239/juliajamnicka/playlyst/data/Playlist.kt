package cz.muni.fi.pv239.juliajamnicka.playlyst.data

data class Playlist(
    val id: Long,
    val name: String,
    val songs: List<Song>
)
