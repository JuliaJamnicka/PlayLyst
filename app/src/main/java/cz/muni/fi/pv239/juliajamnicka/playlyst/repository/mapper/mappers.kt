package cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper

import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Mood
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.PlaylistAndSong
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.*

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

fun Mood.toEntity(): MoodEntity =
    MoodEntity(
        moodId = id,
        name = name,
        color = color,
        acousticness = acousticness?.toInt(),
        danceability = danceability?.toInt(),
        energy = energy?.toInt(),
        instrumentalness = instrumentalness?.toInt(),
        key = key?.toInt(),
        liveness = liveness?.toInt(),
        loudness = loudness?.toInt(),
        mode = mode?.toInt(),
        popularity = popularity?.toInt(),
        speechiness = speechiness?.toInt(),
        tempo = tempo?.toInt(),
        valence = valence?.toInt()
    )

fun MoodEntity.toAppData(): Mood =
    Mood(
        id = moodId,
        name = name,
        color = color,
        acousticness = acousticness,
        danceability = danceability,
        energy = energy,
        instrumentalness = instrumentalness,
        key = key,
        liveness = liveness,
        loudness = loudness,
        mode = mode,
        popularity = popularity,
        speechiness = speechiness,
        tempo = tempo,
        valence = valence
    )
