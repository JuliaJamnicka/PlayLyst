package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
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
            binding.description.visibility = View.VISIBLE
        }

        binding.slider.valueFrom = item.minValue.toFloat()
        binding.slider.valueTo = item.maxValue.toFloat()
       if (item.isDiscrete) {
           binding.slider.stepSize = item.stepSize.toFloat()
       } else {
           binding.slider.stepSize = 0.0f
       }

        binding.slider.value = if (item.value === null)
            (item.defaultValue ?: 0.0).toFloat() else
            (item.value ?: 0.0).toFloat()

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

class OnSliderLetGoListener : Slider.OnSliderTouchListener {

    private lateinit var onSliderChange: (MoodAttribute) -> Unit
    private lateinit var item: MoodAttribute
    fun initialize(onSliderChange: (moodAttribute: MoodAttribute) -> Unit, item: MoodAttribute) {
        this.onSliderChange = onSliderChange
        this.item = item
    }

    @SuppressLint("RestrictedApi")
    override fun onStartTrackingTouch(slider: Slider) {}

    @SuppressLint("RestrictedApi")
    override fun onStopTrackingTouch(slider: Slider) {
        val value = slider.value
        onSliderChange(item.copyNewWithChangedValues(value))
    }
}