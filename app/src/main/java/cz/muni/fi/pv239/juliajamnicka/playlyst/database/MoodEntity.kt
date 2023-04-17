package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val moodId: Long,
    val name: String,
    val color: String,
    val acousticness: Int?,
    val danceability: Int?,
    val energy: Int?,
    val instrumentalness: Int?,
    val key: Int?,
    val liveness: Int?,
    val loudness:Int?,
    val mode: Int?,
    val popularity: Int?,
    val speechiness: Int?,
    val tempo: Int?,
    val valence: Int?
)
