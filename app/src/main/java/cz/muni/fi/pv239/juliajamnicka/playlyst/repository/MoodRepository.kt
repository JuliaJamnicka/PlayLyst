package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.content.Context
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Mood
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttribute
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.MoodDao
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlayLystDatabase
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toAppData
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toEntity

class MoodRepository(
    context : Context,
    private val dao : MoodDao = PlayLystDatabase.create(context).moodDao()
) {
    fun saveOrUpdate(name: String, color: String, attributes: List<MoodAttribute>, id: Long? = null) {
        val mood = Mood(
            id = id ?: 0,
            name = name,
            color = color,
            attributes = attributes
        )
        val moodId = dao.insertMood(mood.toEntity())

        for (attribute in mood.attributes) {
            val moodAttribute = MoodAttribute(
                id = 0,
                moodId = moodId,
                name = attribute.name,
                minValue = attribute.minValue,
                maxValue = attribute.maxValue,
                stepSize = attribute.stepSize,
                canHaveRange = attribute.canHaveRange,
                defaultValue = attribute.defaultValue,
                lowerDefaultValue = attribute.lowerDefaultValue,
                upperDefaultValue = attribute.upperDefaultValue,
                chosenValue = attribute.chosenValue,
                lowerThreshold = attribute.lowerThreshold,
                upperThreshold = attribute.upperThreshold,
            )
            dao.insertMoodAttribute(moodAttribute.toEntity())
        }
    }

    fun delete(mood : Mood) =
        dao.deleteMood(mood.toEntity())

    fun getAllMoods(): List<Mood> =
        dao.getMoodsWithAttributes().map { it.toAppData() }

    fun deleteAllMoods() =
        dao.deleteAllMoods()
}