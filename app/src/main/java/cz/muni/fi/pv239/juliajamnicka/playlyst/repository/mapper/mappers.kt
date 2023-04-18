package cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper

import cz.muni.fi.pv239.juliajamnicka.playlyst.data.*
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

fun MoodWithAttributes.toAppData(): Mood =
    Mood(
        id = mood.moodId,
        name = mood.name,
        color = mood.color,
        attributes = attributes.map { it.toAppData() }
    )

fun Mood.toEntity(): MoodEntity =
    MoodEntity(
        moodId = id,
        name = name,
        color = color,
    )

fun MoodAttributeEntity.toAppData(): MoodAttribute =
    MoodAttribute(
        id = moodAttributeId,
        moodId = moodEntityId,
        name = name,
        minValue = minValue,
        maxValue = maxValue
    )

fun MoodAttribute.toEntity(): MoodAttributeEntity =
    MoodAttributeEntity(
        moodAttributeId = id,
        moodEntityId = moodId,
        name = name,
        minValue = minValue,
        maxValue = maxValue
    )