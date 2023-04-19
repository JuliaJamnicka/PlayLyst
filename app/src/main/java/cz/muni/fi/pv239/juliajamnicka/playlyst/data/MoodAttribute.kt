package cz.muni.fi.pv239.juliajamnicka.playlyst.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoodAttribute(
    val id: Long,
    val moodId: Long,
    val name: String,
    val minValue: Double,
    val maxValue: Double,
    val stepSize: Double,
    val canHaveRange: Boolean,
    val defaultValue: Double? = null,
    val lowerDefaultValue: Double? = null,
    val upperDefaultValue: Double? = null,
    val chosenValue: Double? = null,
    val lowerThreshold : Double? = null,
    val upperThreshold: Double? = null,
) : Parcelable
