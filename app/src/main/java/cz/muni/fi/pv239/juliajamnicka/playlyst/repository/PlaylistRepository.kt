package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.content.Context
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.PlaylistAndSong
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.SongAndArtist
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlayLystDatabase
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlaylistAndSongEntity
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

            for (artist in song.artists) {
                val artistId = dao.insert(artist.toEntity())

                val songAndArtist = SongAndArtist(
                    songId = songId,
                    artistId = artistId
                )
                dao.insert(songAndArtist.toEntity())
            }
        }
    }

    fun getAllPlaylists(): List<Playlist> =
        dao.getPlaylistsWithSongs()
            .map { it.toAppData() }
            .reversed()

    fun deletePlaylist(playlist: Playlist) {
        for (song in playlist.songs) {
            dao.deletePlaylistAndSong(PlaylistAndSongEntity(
                playlistId = playlist.id,
                songId = song.id
            ))
            dao.deleteSong(song.toEntity())
        }
        dao.deletePlaylist(playlist.toEntity())
    }
}