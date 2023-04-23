package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.content.Context
import android.os.Bundle
import android.util.Log
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

    private val searchAdapter: SearchSongsAdapter by lazy {
        SearchSongsAdapter(
            onAddItemClick = { song ->
                if (!chosenSongs.contains(song)) {
                    chosenSongs.add(song)
                    Log.e(song.name, "was chosen")
                    refreshChosen()
                }
            },
            onRemoveItemClick = { song ->
                chosenSongs.remove(song)
                refreshChosen()
            }
        )
    }

    private val chosenAdapter: ChosenSongsAdapter by lazy {
        ChosenSongsAdapter(
            onItemClick = { song ->
                chosenSongs.remove(song)
                Log.e(song.name, "was deleted")
                refreshChosen()
            }
        )
    }

    private val searchRepository: SearchRepository = SearchRepository()
    private var chosenSongs: MutableList<Song> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistCreateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = searchAdapter

        binding.chosenRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chosenRecyclerView.adapter = chosenAdapter
        binding.chosenRecyclerView.visibility = View.VISIBLE

        sendTokenToRepo()
        showIncludeSwitch(false)

        binding.search.setOnCloseListener {
            binding.chosenRecyclerView.visibility = View.VISIBLE

            if (chosenSongs.isNotEmpty()) {
                showIncludeSwitch(true)
            }
            false
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                binding.chosenRecyclerView.visibility = View.VISIBLE
                showIncludeSwitch(chosenSongs.isNotEmpty())
                searchAdapter.submitList(emptyList())
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                binding.chosenRecyclerView.visibility = View.GONE
                showIncludeSwitch(false)

                searchRepository.getSearchResults(query,
                    success = { songs ->
                        searchAdapter.submitList(songs)
                        chosenAdapter.submitList(chosenSongs)
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

    private fun refreshSearch() {
        searchAdapter.submitList(emptyList())
        searchAdapter.notifyDataSetChanged()
    }

    private fun refreshChosen() {
        chosenAdapter.submitList(chosenSongs)
        chosenAdapter.notifyDataSetChanged()
    }

    private fun showIncludeSwitch(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.includeTitle.visibility = visibility
        binding.includeSwitch.visibility = visibility
    }

    override fun onResume() {
        super.onResume()
        refreshSearch()
    }


}