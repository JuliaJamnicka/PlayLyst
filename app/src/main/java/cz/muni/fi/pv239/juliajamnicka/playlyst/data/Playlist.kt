package cz.muni.fi.pv239.juliajamnicka.playlyst.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val spotifyId: String,
    val uri: String,
    val name: String,
    val imageLink: String?,
    val songs: List<Song>
) : Parcelable
