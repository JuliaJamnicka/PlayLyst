package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PLaylistAndSongEntity::class)
    )
    val songs : List<SongEntity>
)
