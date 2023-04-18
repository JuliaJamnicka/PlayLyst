package cz.muni.fi.pv239.juliajamnicka.playlyst.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mood(
    val id: Long,
    val name: String,
    val color: String,
    val attributes: List<MoodAttribute>
) : Parcelable
