package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.content.Context
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.PlaylistAndSong
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlayLystDatabase
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlaylistDao
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toAppData
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toEntity

class PlaylistRepository(
    context : Context,
    private val dao : PlaylistDao = PlayLystDatabase.create(context).playlistDao()
) {

    fun saveOrUpdate(playlist: Playlist) {
        val playlistId = dao.insertPlaylist(playlist.toEntity())

        for (song in playlist.songs) {
            val songId = dao.insertSong(song.toEntity())

            val playlistAndSong = PlaylistAndSong(
                playlistId = playlistId,
                songId = songId
            )
            dao.insert(playlistAndSong.toEntity())
        }
    }

    fun save(name: String, songs: List<Song>, id: Long = 0, imageLink: String? = null) {
        val playlist = Playlist(
            id = id,
            spotifyId = "",
            uri = "",
            name = name,
            imageLink = imageLink,
            songs = songs
        )
        saveOrUpdate(playlist)
    }

    fun getAllPlaylists(): List<Playlist> =
        dao.getPlaylistsWithSongs()
            .map { it.toAppData() }

    fun deletePlaylists() =
        dao.deleteAllPlaylists()

    fun deleteSongs() =
        dao.deleteAllSongs()

    fun deleteAllPlaylistAnSongs() =
        dao.deleteAllPlaylistAndSong()
}