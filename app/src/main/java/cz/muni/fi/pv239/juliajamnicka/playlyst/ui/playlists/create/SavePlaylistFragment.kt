package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSavePlaylistBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.PlaylistRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.SpotifyRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlist.SongsAdapter
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.getResizedBitmap


class SavePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentSavePlaylistBinding

    private val fromGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            processFromGalleryResult(it)
        }

    private val adapter: ChosenSongsAdapter by lazy {
        ChosenSongsAdapter(
            onItemClick = { song ->
                    chosenSongs.remove(song)
                    Log.e(song.name, "was deleted")
                    refreshSongs()
            }
        )
    }

    private val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepository(requireContext())
    }

    private val spotifyRepository: SpotifyRepository = SpotifyRepository()

    private val args: SavePlaylistFragmentArgs by navArgs()
    private lateinit var chosenSongs: MutableList<Song>

    private var playlistImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavePlaylistBinding.inflate(inflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.GONE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chosenSongs = args.songs.toMutableList()

        binding.uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            fromGalleryLauncher.launch(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        refreshSongs()

        sendTokenToRepo()

        binding.saveButton.setOnClickListener {

            spotifyRepository.uploadPlaylist(
                name = binding.nameEditText.text.toString(),
                songs = chosenSongs,
                playlistImage = playlistImage,
                success = { playlist ->
                    playlistRepository.saveOrUpdate(playlist)
                    findNavController().popBackStack(R.id.playlistsFragment, false)
                },
                fail = {
                    Toast.makeText(context, "Error uploading playlist", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun processFromGalleryResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = activityResult.data?.data
            playlistImage = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            binding.playlistCover.setImageBitmap(playlistImage)

            playlistImage = playlistImage!!.getResizedBitmap(400, 400)

            binding.uploadImageButton.text = getString(R.string.create_playlist_change_image_button)
        } else {
            Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshSongs() {
        adapter.submitList(chosenSongs)
        adapter.notifyDataSetChanged()
    }

    private fun sendTokenToRepo() {
        val preferences = this.requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = preferences.getString("token", null) ?: ""

        spotifyRepository.updateAccessToken(token)
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.scrollToPosition(0)
    }

}