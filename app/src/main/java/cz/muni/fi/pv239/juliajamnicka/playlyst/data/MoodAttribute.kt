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
    val isDiscrete: Boolean,
    val defaultValue: Double? = null,
    val lowerDefaultValue: Double,
    val upperDefaultValue: Double,
    var value: Double? = null,
    var lowerValue : Double? = null,
    var upperValue: Double? = null,
) : Parcelable {
    fun copyNewWithChangedValues(newValue: Float)
    : MoodAttribute {
        return MoodAttribute(
            id = id,
            moodId = moodId,
            name = name,
            minValue = minValue,
            maxValue = maxValue,
            stepSize = stepSize,
            isDiscrete = isDiscrete,
            defaultValue = defaultValue,
            lowerDefaultValue = lowerDefaultValue,
            upperDefaultValue = upperDefaultValue,
            value = newValue.toDouble(),
            lowerValue = lowerValue,
            upperValue = upperValue,
        )
    }
}
