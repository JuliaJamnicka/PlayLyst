package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val moodId: Long,
    val name: String,
    val color: String
)
