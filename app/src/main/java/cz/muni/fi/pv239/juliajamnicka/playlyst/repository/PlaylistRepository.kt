package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.content.Context
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.PlaylistAndSong
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PLayLystDatabase
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlaylistDao
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toAppData
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper.toEntity
import java.util.*

class PlaylistRepository(
    context : Context,
    private val dao : PlaylistDao = PLayLystDatabase.create(context).playlistDao()
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

}