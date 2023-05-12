package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSavePlaylistBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.PlaylistRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.SpotifyRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.getResizedBitmap

class SavePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentSavePlaylistBinding

    private val fromGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            processFromGalleryResult(it)
        }

    private val permissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                fromGalleryLauncher.launch(intent)
            }
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.GONE)

        chosenSongs = args.songs.toMutableList()

        binding.uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            if (ContextCompat.checkSelfPermission(
                    requireContext(), READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)
            {
                fromGalleryLauncher.launch(intent)
            } else {
                permissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        refreshSongs()

        binding.nameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.isEmpty() == false) {
                    binding.nameInput.error = null
                }
            }

        })

        binding.saveButton.setOnClickListener {
            if (isNameValid()) {
                binding.saveButton.isClickable = false

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
                        binding.saveButton.isClickable = true
                    }
                )
            }
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

    override fun onResume() {
        super.onResume()
        binding.recyclerView.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.VISIBLE)
    }

    private fun isNameValid(): Boolean {
        val name = binding.nameEditText.text.toString()
        if (name.isEmpty()) {
            binding.nameInput.error = getString(R.string.empty_field)
        }
        return name.isNotEmpty()
    }

}