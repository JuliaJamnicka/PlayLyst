package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttribute
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttributeType
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemMoodAttributeBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.capitalizeFirstLetter
import java.util.*

class MoodAttributesAdapter(
    private val onSliderChange: (moodAttribute: MoodAttribute) -> Unit,
) : ListAdapter<MoodAttribute, MoodAttributeViewHolder>(MoodAttributeDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodAttributeViewHolder =
        MoodAttributeViewHolder(
            ItemMoodAttributeBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MoodAttributeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onSliderChange)
    }

}


class MoodAttributeViewHolder(
    private val binding: ItemMoodAttributeBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val sliderTouchListener = OnSliderLetGoListener()

    fun bind(item: MoodAttribute, onSliderChange: (moodAttribute: MoodAttribute) -> Unit) {
        binding.attributeName.text = item.name.capitalizeFirstLetter()

        val description = MoodAttributeType.valueOf(item.name).getDescription()
        binding.description.text = description

        // TODO: fix disappearing on slider change, why?
        binding.infoButton.setOnClickListener {
            binding.description.visibility = if (binding.description.isVisible)
                View.GONE else
                    View.VISIBLE

            binding.infoButton.isSelected = binding.description.isVisible
        }

        // TODO: make these range sliders
        binding.slider.valueFrom = item.minValue.toFloat()
        binding.slider.valueTo = item.maxValue.toFloat()
       if (item.isDiscrete) {
           binding.slider.stepSize = item.stepSize.toFloat()
       } else {
           binding.slider.stepSize = 0.0f
       }

        val lowerValue = item.lowerValue?.toFloat() ?: item.lowerDefaultValue.toFloat()
        val upperValue = item.upperValue?.toFloat() ?: item.upperDefaultValue.toFloat()
        binding.slider.values = listOf(lowerValue, upperValue)

        sliderTouchListener.initialize(onSliderChange, item)
        binding.slider.addOnSliderTouchListener(sliderTouchListener)

        binding.lowerSliderValue.text = item.minValue.toString()
        binding.upperSliderValue.text = item.maxValue.toString()
    }
}

class MoodAttributeDiffUtil : DiffUtil.ItemCallback<MoodAttribute>() {
    override fun areItemsTheSame(oldItem: MoodAttribute, newItem: MoodAttribute): Boolean =
        oldItem.name === newItem.name

    override fun areContentsTheSame(oldItem: MoodAttribute, newItem: MoodAttribute): Boolean =
        oldItem == newItem
}

class OnSliderLetGoListener : RangeSlider.OnSliderTouchListener {

    private lateinit var onSliderChange: (MoodAttribute) -> Unit
    private lateinit var item: MoodAttribute
    fun initialize(onSliderChange: (moodAttribute: MoodAttribute) -> Unit, item: MoodAttribute) {
        this.onSliderChange = onSliderChange
        this.item = item
    }

    @SuppressLint("RestrictedApi")
    override fun onStartTrackingTouch(slider: RangeSlider) {}

    @SuppressLint("RestrictedApi")
    override fun onStopTrackingTouch(slider: RangeSlider) {
        val values = slider.values
        onSliderChange(item.copyNewWithChangedValues(values))
    }
}