package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoodAttributeEntity(
    @PrimaryKey(autoGenerate = true)
    val moodAttributeId: Long,
    val moodEntityId: Long,
    val name: String,
    val chosenValue: Double? = null,
    val lowerThreshold : Double? = null,
    val upperThreshold: Double? = null,
)
