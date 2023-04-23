package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val songId: Long,
    val spotifyId: String,
    val uri: String,
    val name: String,
    val artist : String,
    val genre : String,
    val imageLink: String?
)
