package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.toLowerCase
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttribute
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttributeType
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemMoodAttributeBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.capitalizeFirstLetter
import java.util.*

class MoodAttributesAdapter(
    private val onItemClick: (MoodAttribute) -> Unit,
) : ListAdapter<MoodAttribute, MoodAttributeViewHolder>(MoodAttributeDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodAttributeViewHolder =
        MoodAttributeViewHolder(
            ItemMoodAttributeBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MoodAttributeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }
}


class MoodAttributeViewHolder(
    private val binding: ItemMoodAttributeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MoodAttribute, onItemClick: (MoodAttribute) -> Unit) {
        binding.attributeName.text = item.name.capitalizeFirstLetter()

        val description = MoodAttributeType.valueOf(item.name).getDescription()
        binding.description.text = description

        binding.infoButton.setOnClickListener {
            binding.description.visibility = if (binding.description.isVisible)
                View.GONE else
                    View.VISIBLE

            binding.infoButton.isSelected = binding.description.isVisible
        }

        if (item.canHaveRange) {
            bindRangeSlider(item)
            binding.rangeSlider.addOnChangeListener { _, _, _ ->
                val (lowerValue, upperValue) = binding.rangeSlider.values
            }
        } else {
            bindSlider(item)
            binding.slider.addOnChangeListener { _, _, _ ->
                val value = binding.slider.value
            }
        }

        binding.lowerSliderValue.text = item.minValue.toString()
        binding.upperSliderValue.text = item.maxValue.toString()

        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }

    private fun bindSlider(item: MoodAttribute) {
        binding.slider.visibility = View.VISIBLE
        binding.rangeSlider.visibility = View.GONE

        binding.slider.valueFrom = item.minValue.toFloat()
        binding.slider.valueTo = item.maxValue.toFloat()
        binding.slider.stepSize = item.stepSize.toFloat()

        val initialValue = if (item.value === null)
            item.defaultValue ?: 0.0 else
                item.value

        binding.slider.value = initialValue.toFloat()
    }

    private fun bindRangeSlider(item: MoodAttribute) {
        binding.rangeSlider.visibility = View.VISIBLE
        binding.slider.visibility = View.GONE

        binding.rangeSlider.valueFrom = item.minValue.toFloat()
        binding.rangeSlider.valueTo = item.maxValue.toFloat()

        binding.rangeSlider.values = if (item.lowerValue !== null && item.upperValue != null)
            listOf(item.lowerValue.toFloat(), item.upperValue.toFloat()) else
                listOf(item.lowerDefaultValue?.toFloat(), item.upperDefaultValue?.toFloat())

    }
}


class MoodAttributeDiffUtil : DiffUtil.ItemCallback<MoodAttribute>() {
    override fun areItemsTheSame(oldItem: MoodAttribute, newItem: MoodAttribute): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MoodAttribute, newItem: MoodAttribute): Boolean =
        oldItem == newItem
}