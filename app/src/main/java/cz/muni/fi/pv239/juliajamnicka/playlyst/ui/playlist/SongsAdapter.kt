package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemPlaylistSongBinding

class SongsAdapter(
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
        //binding.songCover =
        binding.songName.text = item.name
        binding.songArtist.text = item.artist
        binding.genreChip.text = item.genre

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
