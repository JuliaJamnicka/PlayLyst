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

    // Insert a playlist with its associated songs
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistWithSongs(playlistWithSongs: PlaylistWithSongs)

    @Transaction
    @Delete
    fun deletePlaylistWithSongs(playlistWithSongs: PlaylistWithSongs)

    @Transaction
    @Query("SELECT * FROM playlist WHERE playlistId = :playlistId")
    fun getPlaylistWithSongsById(playlistId: Long): PlaylistWithSongs

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): List<PlaylistEntity>
}