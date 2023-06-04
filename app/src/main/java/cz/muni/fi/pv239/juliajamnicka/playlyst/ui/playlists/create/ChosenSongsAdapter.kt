package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemPlaylistSongBinding

class ChosenSongsAdapter(
    private val onItemClick: (Song) -> Unit,
    private val onPlayClick: (Song) -> Unit,
) : ListAdapter<Song, ChosenSongViewHolder>(ChosenSongDiffUtil()) {
    var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChosenSongViewHolder =
        ChosenSongViewHolder(
            ItemPlaylistSongBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: ChosenSongViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick, onPlayClick, position == selectedPosition)

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_song_background)
        } else {
            holder.itemView.setBackgroundResource(0)
        }

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            onPlayClick(item)

            selectedPosition = if (previousSelectedPosition == position) {
                RecyclerView.NO_POSITION
            } else {
                position
            }

            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

}


class ChosenSongViewHolder(
    private val binding: ItemPlaylistSongBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song, onItemClick: (Song) -> Unit, onPlayClick: (Song) -> Unit, isSelected: Boolean) {
        binding.songCover.load(item.imageLink) {
            error(R.drawable.blank_song_cover)
        }
        binding.songName.text = item.name
        binding.songArtist.text = item.getArtistNames()

        binding.addButton.setImageResource(R.drawable.ic_remove)
        binding.addButton.isSelected = true
        binding.addButton.visibility = View.VISIBLE
        if (isSelected) {
            binding.addButton.imageTintList = ColorStateList.valueOf(Color.WHITE)
        } else {
            binding.addButton.imageTintList = ContextCompat.getColorStateList(context, R.color.purple_500)
        }

        binding.addButton.setOnClickListener {
            onItemClick(item)
        }
    }
}


class ChosenSongDiffUtil : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem == newItem
}
