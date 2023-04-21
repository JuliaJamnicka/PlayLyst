package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ItemPlaylistBinding


class PlaylistsAdapter(
    private val onItemClick: (Playlist) -> Unit,
) : ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(
            ItemPlaylistBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }
}


class PlaylistViewHolder(
    private val binding: ItemPlaylistBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist, onItemClick: (Playlist) -> Unit) {
        binding.playlistCover.load(item.imageLink) {
            error(R.drawable.blank_song_cover)
        }
        binding.playlistName.text = item.name

        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}


class PlaylistDiffUtil : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
        oldItem == newItem
}