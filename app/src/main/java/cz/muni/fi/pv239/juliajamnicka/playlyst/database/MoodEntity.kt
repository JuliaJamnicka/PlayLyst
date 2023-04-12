package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
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
