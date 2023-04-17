package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.*

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(entity: PlaylistEntity): Long

    @Update
    fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    fun deletePlaylist(playlist: PlaylistEntity)

    @Insert
    fun insertSong(song: SongEntity): Long

    @Delete
    fun deleteSong(song: SongEntity)

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Query("SELECT * FROM PlaylistEntity")
    fun getAllPlaylists(): List<PlaylistEntity>
}