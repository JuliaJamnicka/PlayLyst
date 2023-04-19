package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttribute
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemMoodAttributeBinding

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
        binding.attributeName.text = item.name
        binding.slider.valueFrom = item.minValue.toFloat()
        binding.slider.valueTo = item.maxValue.toFloat()
        binding.slider.stepSize = item.stepSize.toFloat()
        binding.slider.values = listOf(item.lowerDefaultValue?.toFloat(), item.upperDefaultValue?.toFloat())

        binding.lowerSliderValue.text = item.minValue.toString()
        binding.upperSliderValue.text = item.maxValue.toString()

        binding.root.setOnClickListener {
            onItemClick(item)
        }

        binding.slider.addOnChangeListener { _, _, _ ->
            val (lowerValue, upperValue) = binding.slider.values

        }
    }
}


class MoodAttributeDiffUtil : DiffUtil.ItemCallback<MoodAttribute>() {
    override fun areItemsTheSame(oldItem: MoodAttribute, newItem: MoodAttribute): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MoodAttribute, newItem: MoodAttribute): Boolean =
        oldItem == newItem
}