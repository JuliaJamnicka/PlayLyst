package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentPlaylistCreateBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.SearchRepository


class PlaylistCreateFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistCreateBinding

    private val adapter: SearchSongsAdapter by lazy {
        SearchSongsAdapter(
            onItemClick = {}
        )
    }

    private val searchRepository: SearchRepository = SearchRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistCreateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        sendTokenToRepo()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchRepository.getSearchResults(query,
                    success = { songs -> adapter.submitList(songs)
                    },
                    fail = {
                        Toast.makeText(context,
                            "Error getting search results", Toast.LENGTH_SHORT).show()
                    }
                )
                return false
            }
        })


    }

    private fun sendTokenToRepo() {
        val preferences = this.requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = preferences.getString("token", null) ?: ""

        searchRepository.updateAccessToken(token)
    }

    private fun refreshList() {
        adapter.submitList(emptyList())
    }


}