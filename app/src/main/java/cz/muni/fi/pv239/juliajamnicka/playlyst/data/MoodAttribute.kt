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
    val value: Double? = null,
    val lowerValue : Double? = null,
    val upperValue: Double? = null,
) : Parcelable {
    fun isValueChanged(): Boolean {
        return value !== null || (lowerValue !== null && upperValue !== null)
    }
}
