package cz.muni.fi.pv239.juliajamnicka.playlyst.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val spotifyId: String,
    val uri: String,
    val name: String,
    val artist: String, // change this later
    val genre: String,
    val imageLink: String?
): Parcelable
