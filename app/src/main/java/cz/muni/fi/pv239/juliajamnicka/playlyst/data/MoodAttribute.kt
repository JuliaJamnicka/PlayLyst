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
    val isFloat: Boolean,
    val isDiscrete: Boolean,
    val defaultValue: Double? = null,
    val lowerDefaultValue: Double,
    val upperDefaultValue: Double,
    var value: Double? = null,
    var lowerValue : Double? = null,
    var upperValue: Double? = null,
) : Parcelable {
    fun copyNewWithChangedValues(values: List<Float>)
    : MoodAttribute {
        return MoodAttribute(
            id = id,
            moodId = moodId,
            name = name,
            minValue = minValue,
            maxValue = maxValue,
            stepSize = stepSize,
            isFloat = isFloat,
            isDiscrete = isDiscrete,
            defaultValue = defaultValue,
            lowerDefaultValue = lowerDefaultValue,
            upperDefaultValue = upperDefaultValue,
            value = value,
            lowerValue = values[0].toDouble(),
            upperValue = values[1].toDouble()
        )
    }
}
