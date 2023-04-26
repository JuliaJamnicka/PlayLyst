package cz.muni.fi.pv239.juliajamnicka.playlyst.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val id: Long,
    val spotifyId: String,
    val name: String,
    val uri: String,
    val imageLink: String?
) : Parcelable
