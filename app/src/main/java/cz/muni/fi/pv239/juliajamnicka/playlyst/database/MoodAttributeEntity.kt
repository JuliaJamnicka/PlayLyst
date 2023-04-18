package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoodAttributeEntity(
    @PrimaryKey(autoGenerate = true)
    val moodAttributeId: Long,
    val moodEntityId: Long,
    val name: String,
    val minValue: Float,
    val maxValue: Float
)
