package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Playlist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentPlaylistsBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.PlaylistRepository

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding

    private val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepository(requireContext())
    }

    private val adapter: PlaylistsAdapter by lazy {
        PlaylistsAdapter(
            onItemClick = { playlist ->
                    findNavController().navigate(
                        PlaylistsFragmentDirections
                            .actionPlaylistsFragmentToSongsFragment(playlist = playlist))
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        playlistRepository.deletePlaylists()
        playlistRepository.deleteSongs()
        playlistRepository.deleteAllPlaylistAnSongs()

        playlistRepository.saveOrUpdate(
            Playlist(
                id = "spotify-id",
                uri = "hello",
                name = "Car radio",
                imageLink = "https://i.ibb.co/1dcHGDv/car-radio.png",
                songs = listOf<Song>(Song(id = "id", uri = "", name = "Young and Beautiful",
                artist = "Lana Del Rey", genre = "Pop", imageLink = ""))
            )
        )
        playlistRepository.saveOrUpdate(
            Playlist(
                id = "spotify-id-2",
                uri = "hello",
                name = "Dancing alone in the kitchen",
                imageLink = "https://i.ibb.co/wzD3w2w/tumblr-f4bb183a62a9600b538875a58e9a7bcb-ec6afa71-540.jpg",
                songs = emptyList()
            ),
        )
        refreshList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

    }

    private fun refreshList() {
        adapter.submitList(playlistRepository.getAllPlaylists())
    }
}