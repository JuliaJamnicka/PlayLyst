package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SongWithArtists(
    @Embedded val song: SongEntity,

    @Relation(
        parentColumn = "songId",
        entityColumn = "artistId",
        associateBy = Junction(SongAndArtistEntity::class)
    )
    val artists: List<ArtistEntity>
)
