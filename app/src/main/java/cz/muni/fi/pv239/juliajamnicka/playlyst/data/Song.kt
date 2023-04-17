package cz.muni.fi.pv239.juliajamnicka.playlyst.data

data class Song(
    val id: String,
    val uri: String,
    val name: String,
    val artist: String, // change this later
    val genre: String,
    val imageLink: String?
) {}
