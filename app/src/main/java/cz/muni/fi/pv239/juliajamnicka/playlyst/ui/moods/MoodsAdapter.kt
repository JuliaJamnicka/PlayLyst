package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Mood
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemMoodBinding

class MoodsAdapter(
    private val onItemClick: (Mood) -> Unit,
) : ListAdapter<Mood, MoodViewHolder>(MoodDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder =
        MoodViewHolder(
            ItemMoodBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }
}


class MoodViewHolder(
    private val binding: ItemMoodBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Mood, onItemClick: (Mood) -> Unit) {
        binding.moodName.text = item.name
        binding.moodsIconBig.setColorFilter(Color.parseColor(item.color))

        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}


class MoodDiffUtil : DiffUtil.ItemCallback<Mood>() {
    override fun areItemsTheSame(oldItem: Mood, newItem: Mood): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Mood, newItem: Mood): Boolean =
        oldItem == newItem
}
