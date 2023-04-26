package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemPlaylistSongBinding

class ChosenSongsAdapter(
    private val onItemClick: (Song) -> Unit,
) : ListAdapter<Song, ChosenSongViewHolder>(ChosenSongDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChosenSongViewHolder =
        ChosenSongViewHolder(
            ItemPlaylistSongBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ChosenSongViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }

}


class ChosenSongViewHolder(
    private val binding: ItemPlaylistSongBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song, onItemClick: (Song) -> Unit) {
        binding.songCover.load(item.imageLink) {
            error(R.drawable.blank_song_cover)
        }
        binding.songName.text = item.name
        binding.songArtist.text = item.getArtistNames()

        binding.addButton.setImageResource(R.drawable.ic_remove)
        binding.addButton.isSelected = true
        binding.addButton.visibility = View.VISIBLE

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
