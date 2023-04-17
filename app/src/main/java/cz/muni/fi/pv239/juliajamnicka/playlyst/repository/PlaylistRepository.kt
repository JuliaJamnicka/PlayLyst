package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.content.Context
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.PlaylistAndSong
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlayLystDatabase
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlaylistDao
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toAppData
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toEntity

class PlaylistRepository(
    context : Context,
    private val dao : PlaylistDao = PlayLystDatabase.create(context).playlistDao()
) {

    fun saveOrUpdate(playlist: Playlist) {
        dao.insertPlaylist(playlist.toEntity())

        for (song in playlist.songs) {
            dao.insertSong(song.toEntity())

            val playlistAndSong = PlaylistAndSong(
                playlistId = playlist.id,
                songId = song.id
            )
            dao.insert(playlistAndSong.toEntity())
        }
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