package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.content.Context
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Mood
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.MoodDao
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlayLystDatabase
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toAppData
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toEntity

class MoodRepository(
    context : Context,
    private val dao : MoodDao = PlayLystDatabase.create(context).moodDao()
) {
    fun saveOrUpdate(mood : Mood) {
        dao.insertMood(mood.toEntity())

        for (attribute in mood.attributes) {
            dao.insertMoodAttribute(attribute.toEntity())
        }
    }

    fun delete(mood : Mood) =
        dao.deleteMood(mood.toEntity())

    fun getAllMoods(): List<Mood> =
        dao.getMoodsWithAttributes().map { it.toAppData() }

    fun deleteAllMoods() =
        dao.deleteAllMoods()
}