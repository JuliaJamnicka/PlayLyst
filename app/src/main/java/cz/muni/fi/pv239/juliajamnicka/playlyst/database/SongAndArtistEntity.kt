package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity

@Entity(primaryKeys = ["songId", "artistId"],)
data class SongAndArtistEntity(
    val songId: Long,
    val artistId: Long
)
