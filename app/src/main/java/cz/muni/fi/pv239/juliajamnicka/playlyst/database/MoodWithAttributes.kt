package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Embedded
import androidx.room.Relation

data class MoodWithAttributes(
    @Embedded val mood: MoodEntity,

    @Relation(
        parentColumn = "moodId",
        entityColumn = "moodEntityId"
    )
    val attributes: List<MoodAttributeEntity>
)
