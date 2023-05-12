package cz.muni.fi.pv239.juliajamnicka.playlyst.repository.mapper

import cz.muni.fi.pv239.juliajamnicka.playlyst.data.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.database.*

fun PlaylistWithSongs.toAppData(): Playlist =
    Playlist(
        id = playlist.playlistId,
        spotifyId = playlist.spotifyId,
        uri = playlist.uri,
        name = playlist.name,
        imageLink = playlist.imageLink,
        songs = songs.map { it.toAppData() }
    )

fun Playlist.toEntity(): PlaylistEntity =
    PlaylistEntity(
        playlistId = id,
        spotifyId = spotifyId,
        uri = uri,
        name = name,
        imageLink = imageLink
    )

fun SongWithArtists.toAppData() : Song =
    Song(
        id = song.songId,
        spotifyId = song.spotifyId,
        uri = song.uri,
        name = song.name,
        genre = song.genre,
        imageLink = song.imageLink,
        artists = artists.map { it.toAppData() }
    )

fun Song.toEntity() : SongEntity =
    SongEntity(
        songId = id,
        spotifyId = spotifyId,
        uri = uri,
        name = name,
        genre = genre,
        imageLink = imageLink
    )

fun PlaylistAndSong.toEntity(): PlaylistAndSongEntity =
    PlaylistAndSongEntity(
        playlistId = playlistId,
        songId = songId
    )


fun PlaylistAndSongEntity.ToAppData(): PlaylistAndSong =
    PlaylistAndSong(
        playlistId = playlistId,
        songId = songId
    )


fun Artist.toEntity(): ArtistEntity =
    ArtistEntity(
        artistId = id,
        spotifyId, name, uri, imageLink
    )

fun ArtistEntity.toAppData() =
    Artist(
        id = artistId,
        spotifyid, name, uri, imageLink
    )

fun SongAndArtist.toEntity() =
    SongAndArtistEntity(
        songId, artistId
    )

fun SongAndArtistEntity.toAppData() =
    SongAndArtist(
        songId, artistId
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

fun MoodAttributeEntity.toAppData(): MoodAttribute {
    val thresholds = MoodAttributeType.valueOf(name).getThresholds()
    return MoodAttribute(
        id = moodAttributeId,
        moodId = moodEntityId,
        name = name,
        minValue = thresholds.minValue,
        maxValue = thresholds.maxValue,
        stepSize = thresholds.stepSize,
        isFloat = thresholds.isFloat,
        isDiscrete = thresholds.isDiscrete,
        lowerDefaultValue = thresholds.lowerDefaultValue,
        upperDefaultValue = thresholds.upperDefaultValue,
        value = chosenValue,
        lowerValue = lowerThreshold,
        upperValue = upperThreshold
    )
}

fun MoodAttribute.toEntity(): MoodAttributeEntity =
    MoodAttributeEntity(
        moodAttributeId = id,
        moodEntityId = moodId,
        name, value, lowerValue, upperValue
    )