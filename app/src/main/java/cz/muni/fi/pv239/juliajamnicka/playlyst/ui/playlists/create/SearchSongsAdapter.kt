package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemPlaylistSongBinding

class SearchSongsAdapter(
    private val onItemClick: (Song) -> Unit,
) : ListAdapter<Song, SongViewHolder>(SongDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder =
        SongViewHolder(
            ItemPlaylistSongBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }
}


class SongViewHolder(
    private val binding: ItemPlaylistSongBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song, onItemClick: (Song) -> Unit) {
        binding.songCover.load(item.imageLink) {
            error(R.drawable.blank_song_cover)
        }
        binding.songName.text = item.name
        binding.songArtist.text = item.artist

        binding.addButton.setImageDrawable(R.drawable.ic_add_song_selector.toDrawable())

        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}


class SongDiffUtil : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem == newItem
}
