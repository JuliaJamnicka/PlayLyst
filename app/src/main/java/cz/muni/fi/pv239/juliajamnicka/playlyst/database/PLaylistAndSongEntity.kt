package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "songId"],)
data class PLaylistAndSongEntity(
    val playlistId: Long,
    val songId: Long
)
