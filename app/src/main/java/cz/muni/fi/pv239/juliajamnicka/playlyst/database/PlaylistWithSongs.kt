package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,

    @Relation(
        entity = SongEntity::class,
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistAndSongEntity::class)
    )
    val songs: List<SongWithArtists>
)
