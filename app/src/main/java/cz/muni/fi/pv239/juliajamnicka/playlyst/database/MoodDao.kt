package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttribute

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMood(entity: MoodEntity): Long

    @Update
    fun updateMood(mood: MoodEntity)

    @Delete
    fun deleteMood(mood: MoodEntity)

    @Transaction
    @Query("SELECT * FROM MoodEntity")
    fun getMoodsWithAttributes(): List<MoodWithAttributes>

    @Query("DELETE FROM MoodEntity")
    fun deleteAllMoods()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoodAttribute(moodAttributeEntity: MoodAttributeEntity)

    @Delete
    fun deleteMoodAttribute(moodAttributeEntity: MoodAttributeEntity)

    @Query("SELECT * FROM MoodAttributeEntity")
    fun getAllMoodAttributes(): List<MoodAttributeEntity>
}