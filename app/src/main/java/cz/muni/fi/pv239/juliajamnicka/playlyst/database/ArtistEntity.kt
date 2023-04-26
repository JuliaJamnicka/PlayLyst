package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true)
    val artistId: Long,
    val spotifyid: String,
    val name: String,
    val uri: String,
    val imageLink: String?
)
