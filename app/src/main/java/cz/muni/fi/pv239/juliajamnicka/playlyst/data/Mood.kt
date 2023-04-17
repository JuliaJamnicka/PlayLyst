package cz.muni.fi.pv239.juliajamnicka.playlyst.data

data class Mood(
    val id: Long,
    val name: String,
    val color: String,
    val acousticness: Number?,
    val danceability: Number?,
    val energy: Number?,
    val instrumentalness: Number?,
    val key: Number?,
    val liveness: Number?,
    val loudness:Number?,
    val mode: Number?,
    val popularity: Number?,
    val speechiness: Number?,
    val tempo: Number?,
    val valence: Number?
)