package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.*

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMood(entity: MoodEntity): Long

    @Query("SELECT * FROM MoodEntity WHERE moodId = :moodId")
    fun getMoodById(moodId: Long): MoodEntity

    @Update
    fun updateMood(mood: MoodEntity)

    @Delete
    fun deleteMood(mood: MoodEntity)
}