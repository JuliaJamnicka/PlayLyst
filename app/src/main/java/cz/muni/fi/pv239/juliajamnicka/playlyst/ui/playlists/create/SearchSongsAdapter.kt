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

class SearchSongsAdapter(
    private val onAddItemClick: (Song) -> Unit,
    private val onRemoveItemClick: (Song) -> Unit,
) : ListAdapter<Song, SearchSongViewHolder>(SearchSongDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder =
        SearchSongViewHolder(
            ItemPlaylistSongBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SearchSongViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onAddItemClick, onRemoveItemClick)
    }
}


class SearchSongViewHolder(
    private val binding: ItemPlaylistSongBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song, onAddItemClick: (Song) -> Unit, onRemoveItemClick: (Song) -> Unit) {
        fun changeSongSelection() {
            if (!binding.addButton.isSelected) {
                onAddItemClick(item)
                binding.addButton.isSelected = true
            } else {
                onRemoveItemClick(item)
                binding.addButton.isSelected = false
            }
        }

        binding.songCover.load(item.imageLink) {
            error(R.drawable.blank_song_cover)
        }
        binding.songName.text = item.name
        binding.songArtist.text = item.getArtistNames()

        binding.addButton.isSelected = false
        binding.addButton.visibility = View.VISIBLE

        binding.addButton.setOnClickListener {
            changeSongSelection()
        }

        binding.root.setOnClickListener {
            changeSongSelection()
        }
    }
}


class SearchSongDiffUtil : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem == newItem
}
