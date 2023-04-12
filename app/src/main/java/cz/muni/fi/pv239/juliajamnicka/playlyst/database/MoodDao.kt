package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.*

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMood(entity: MoodEntity): Long

    @Query("SELECT * FROM mood WHERE moodId = :moodId")
    fun getMoodById(moodId: Long): MoodEntity

    @Update
    fun updateMood(playlist: MoodEntity)

    @Delete
    fun deletePlaylist(playlist: PlaylistEntity)
}