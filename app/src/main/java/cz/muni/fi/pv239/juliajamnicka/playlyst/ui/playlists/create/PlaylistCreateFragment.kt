package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Mood
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.MoodAttribute
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentPlaylistCreateBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.SpotifyRepository

private const val MAX_SEED_COUNT = 5

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

    private val spotifyRepository: SpotifyRepository by lazy {
        SpotifyRepository()
    }

    private var chosenSongs: MutableList<Song> = mutableListOf()
    private var chosenAttributes: List<MoodAttribute> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistCreateBinding.inflate(layoutInflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.GONE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isNetworkAvailable()) {
            val dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.no_internet_alert_dialog, null)

            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded).apply {
                setView(dialogView)
            }.create()

            dialogView.findViewById<Button>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
                findNavController().navigateUp()
            }

            dialogView.findViewById<Button>(R.id.positive_button).setOnClickListener {
                dialog.dismiss()
                findNavController().navigateUp()
                findNavController().navigate(R.id.playlistCreateFragment)
            }
            dialog.show()
        }

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = searchAdapter

        binding.chosenRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chosenRecyclerView.adapter = chosenAdapter
        binding.chosenRecyclerView.visibility = View.VISIBLE


        setUpSearchStyle()

        binding.search.setOnCloseListener {
            binding.chosenRecyclerView.visibility = View.VISIBLE
            binding.saveButton.visibility = View.VISIBLE

            if (chosenSongs.isNotEmpty()) {
                showNoSearchScreen(true)
            }
            false
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // TODO: implement debounce and partial loading
            override fun onQueryTextChange(newText: String): Boolean {
                hideSearch()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                binding.chosenRecyclerView.visibility = View.GONE
                showNoSearchScreen(false)
                binding.saveButton.visibility = View.GONE
                binding.searchDoneButton.visibility = View.VISIBLE

                spotifyRepository.getSearchResults(query,
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

        binding.searchDoneButton.setOnClickListener {
            hideSearch()
            binding.scrollview.fullScroll(ScrollView.FOCUS_UP)
        }

        spotifyRepository.getGenreSeeds(
            success = { genres -> showGenres(genres) },
            fail = {}
        )

        binding.moodButton.setOnClickListener {
            findNavController().navigate(PlaylistCreateFragmentDirections
                .actionPlaylistCreateFragmentToCreatePlaylistAddMoodBottomSheet())
        }


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Mood>("mood")
            ?.observe(viewLifecycleOwner) {
                chosenAttributes = it.attributes

                binding.moodTitle.text = it.name
                binding.moodTitle.setTextColor(Color.parseColor(it.color))
            }


        binding.saveButton.setOnClickListener {
            binding.saveButton.isClickable = false

            fun createGenres() = binding.genresChipGroup.children
                .map { it as Chip }
                .filter { it.isChecked }
                .map { it.text.toString() }
                .toList()
            val genres = createGenres()

            if (areSeedsValid(genres)) {
                generateSongs(genres)
            } else {
                Toast.makeText(requireContext(),
                    "Number of seeds needs to be between 1 and 5.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun setUpSearchStyle() {
        // it wasn't working any other way
        val id = requireContext().resources.getIdentifier("android:id/search_src_text", null, null)
        val searchText = binding.search.findViewById<TextView>(id)
        if (searchText != null) {
            searchText.typeface = ResourcesCompat.getFont(requireContext(), R.font.circular)
            searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
    }

    private fun hideSearch() {
        binding.chosenRecyclerView.visibility = View.VISIBLE
        binding.searchDoneButton.visibility = View.GONE
        binding.saveButton.visibility = View.VISIBLE
        showNoSearchScreen(chosenSongs.isNotEmpty())
        searchAdapter.submitList(emptyList())
    }

    private fun showGenres(genres: List<String>) {
        for (genre in genres) {
            val chip = layoutInflater.inflate(R.layout.genre_chip, binding.genresChipGroup, false) as Chip

            val uncheckedColor =  AppCompatResources.getColorStateList(requireContext(), R.color.transparent_purple)
            val checkedColor = AppCompatResources.getColorStateList(requireContext(), R.color.light_salmon)

            chip.apply {
                id = ViewCompat.generateViewId()
                width = LayoutParams.WRAP_CONTENT
                height = LayoutParams.WRAP_CONTENT
                chipBackgroundColor = uncheckedColor
                invalidateOutline()
                text = genre
                isCheckable = true
                isClickable = true
                setOnCheckedChangeListener { _, _ ->
                    chipBackgroundColor = if (isChecked) checkedColor else uncheckedColor
                    sortGenres()
                }
            }

            binding.genresChipGroup.addView(chip)
        }
    }

    private fun sortGenres() {
        val genres = binding.genresChipGroup.children.sortedBy { !(it as Chip).isChecked }

       for (genre in genres) {
           binding.genresChipGroup.removeView(genre)
           binding.genresChipGroup.addView(genre)
       }
    }

    private fun generateSongs(genres: List<String>) {
        // TODO: add getting of more results
        spotifyRepository.getRecommendations(
            songs = chosenSongs,
            genres = genres,
            attributes = chosenAttributes,
            success = { (_, songs) ->
                val finalSongs = if (binding.includeSwitch.isChecked)
                    chosenSongs + songs else songs
                findNavController().navigate(PlaylistCreateFragmentDirections
                    .actionPlaylistCreateFragmentToSavePlaylistFragment(finalSongs.toTypedArray()))
            },
            fail = {
                Toast.makeText(context,
                    "Error generating songs", Toast.LENGTH_SHORT).show()
                binding.saveButton.isClickable = true
            }
        )
    }

    private fun refreshSearch() {
        searchAdapter.submitList(emptyList())
        searchAdapter.notifyDataSetChanged()
    }

    private fun refreshChosen() {
        chosenAdapter.submitList(chosenSongs)
        chosenAdapter.notifyDataSetChanged()
    }

    private fun showNoSearchScreen(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.includeTitle.visibility = visibility
        binding.includeSwitch.visibility = visibility

        binding.genresTitle.visibility = visibility
        binding.genresScroll.visibility = visibility

        binding.moodTitle.visibility = visibility
        binding.moodButton.visibility = visibility
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE)
        refreshSearch()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun areSeedsValid(genres: List<String>): Boolean {
        val seedCount = chosenSongs.size + genres.size

        return seedCount in 1..MAX_SEED_COUNT
    }
}