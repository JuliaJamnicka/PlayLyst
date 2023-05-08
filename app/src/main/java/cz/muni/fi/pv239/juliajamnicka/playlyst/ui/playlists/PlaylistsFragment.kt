package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        binding.createButton.setOnClickListener {
            findNavController().
                navigate(PlaylistsFragmentDirections.actionPlaylistsFragmentToPlaylistCreateFragment())
        }

    }

    private fun refreshList() {
        adapter.submitList(playlistRepository.getAllPlaylists())
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }
}