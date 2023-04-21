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
    var value: Double? = null,
    var lowerValue : Double? = null,
    var upperValue: Double? = null,
) : Parcelable {
    fun isValueChanged(): Boolean {
        return value !== null || (lowerValue !== null && upperValue !== null)
    }

    fun copyNewWithChangedValues(value: Float)
    : MoodAttribute {
        return MoodAttribute(
            id = id,
            moodId = moodId,
            name = name,
            minValue = minValue,
            maxValue = maxValue,
            stepSize = stepSize,
            canHaveRange = canHaveRange,
            defaultValue = defaultValue,
            lowerDefaultValue = lowerDefaultValue,
            upperDefaultValue = upperDefaultValue,
            value = value.toDouble(),
        )
    }
}
