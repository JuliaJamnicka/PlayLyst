package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.*

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMood(entity: MoodEntity): Long

    @Query("SELECT * FROM MoodEntity WHERE moodId = :moodId")
    fun getMoodById(moodId: Long): MoodEntity

    @Query("SELECT * FROM MoodEntity")
    fun getAllMoods(): List<MoodEntity>

    @Update
    fun updateMood(mood: MoodEntity)

    @Delete
    fun deleteMood(mood: MoodEntity)

    @Query("DELETE FROM MoodEntity")
    fun deleteAllMoods()
}