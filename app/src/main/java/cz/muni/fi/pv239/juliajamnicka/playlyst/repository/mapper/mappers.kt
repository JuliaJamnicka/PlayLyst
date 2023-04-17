package cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper

import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.PlaylistAndSong
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PLaylistAndSongEntity
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlaylistEntity
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.PlaylistWithSongs
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.SongEntity

fun PlaylistWithSongs.toAppData(): Playlist =
    Playlist(
        id = playlist.playlistId,
        uri = playlist.uri,
        name = playlist.name,
        imageLink = playlist.imageLink,
        songs = songs.map { it.toAppData() }
    )

fun Playlist.toEntity(): PlaylistEntity =
    PlaylistEntity(
        playlistId = id,
        uri = uri,
        name = name,
        imageLink = imageLink
    )

fun SongEntity.toAppData() : Song =
    Song(
        id = songId,
        uri = uri,
        name = name,
        artist = artist,
        genre = genre,
        imageLink = imageLink
    )

fun Song.toEntity() : SongEntity =
    SongEntity(
        songId = id,
        uri = uri,
        name = name,
        artist = artist,
        genre = genre,
        imageLink = imageLink
    )

fun PlaylistAndSong.toEntity(): PLaylistAndSongEntity =
    PLaylistAndSongEntity(
        playlistId = playlistId,
        songId = songId
    )

// i dont think im gonna need this everrr
fun PLaylistAndSongEntity.ToAppData(): PlaylistAndSong =
    PlaylistAndSong(
        playlistId = playlistId,
        songId = songId
    )
