package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSongsBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.PlaylistRepository

class SongsFragment : Fragment() {
    private lateinit var binding: FragmentSongsBinding

    private val args: SongsFragmentArgs by navArgs()

    private val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepository(requireContext())
    }

    private val adapter: SongsAdapter by lazy {
        SongsAdapter(
            onItemClick = { song ->
                {}
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongsBinding.inflate(layoutInflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.show()
        mainActivity.title = ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistCover.load(args.playlist?.imageLink) {
            error(R.drawable.blank_song_cover)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        refreshList()

    }

    private fun refreshList() {
        adapter.submitList(args.playlist?.songs)
    }

}