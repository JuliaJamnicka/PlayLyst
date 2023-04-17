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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: SongEntity): Long

    @Delete
    fun deleteSong(song: SongEntity)

    @Insert
    fun insert(pLaylistAndSongEntity: PLaylistAndSongEntity)

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Query("SELECT * FROM PlaylistEntity")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM PlaylistEntity")
    fun deleteAllPlaylists()
}