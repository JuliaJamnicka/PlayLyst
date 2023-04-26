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
    fun insert(pLaylistAndSongEntity: PlaylistAndSongEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artistEntity: ArtistEntity) : Long

    @Delete
    fun deleteArtist(artist: ArtistEntity)

    @Insert
    fun insert(songAndArtistEntity: SongAndArtistEntity)

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Query("SELECT * FROM PlaylistEntity")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM PlaylistEntity")
    fun deleteAllPlaylists()

    @Query("DELETE FROM SongEntity")
    fun deleteAllSongs()

    @Query("DELETE FROM PlaylistAndSongEntity")
    fun deleteAllPlaylistAndSong()

    @Transaction
    @Query("SELECT * FROM SongEntity")
    fun getSongsWithArtists(): List<SongWithArtists>
}