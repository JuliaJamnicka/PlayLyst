package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.PrimaryKey

data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val uri: String,
    val name: String,
    val imageLink: String
)
