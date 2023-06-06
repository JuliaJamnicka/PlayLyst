package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSavePlaylistBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.PlaylistRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.SpotifyRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.getResizedBitmap
import com.spotify.android.appremote.api.SpotifyAppRemote
import cz.muni.fi.pv239.juliajamnicka.playlyst.BuildConfig

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
                val position = chosenSongs.indexOf(song)
                chosenSongs.remove(song)

                adapter.apply {
                    if (position == selectedPosition) {
                        selectedPosition = RecyclerView.NO_POSITION
                        spotifyAppRemote?.playerApi?.pause()
                    } else if (position < selectedPosition) {
                        selectedPosition--
                    }
                }

                Log.e(song.name, "was deleted")
                refreshSongs()
            },
            onPlayClick = { song ->
                val position = chosenSongs.indexOf(song)

                if (position == adapter.selectedPosition) {
                    spotifyAppRemote?.playerApi?.pause()
                } else {
                    spotifyAppRemote?.playerApi?.play(song.uri)
                }
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

    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavePlaylistBinding.inflate(inflater, container, false)

        initializeSpotifyAppRemote()

        return binding.root
    }

    private fun initializeSpotifyAppRemote() {
        SpotifyAppRemote.connect(
            requireContext(), ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
            .setRedirectUri(BuildConfig.REDIRECT_URI)
            .showAuthView(false)
            .build(),
            object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    spotifyAppRemote = appRemote
                }

                override fun onFailure(throwable: Throwable) {}
            })
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

                val dialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.playlist_save_loading_dialog, null)

                val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded).apply {
                    setView(dialogView)
                    setCancelable(false)
                }.create()

                dialog.show()

                spotifyRepository.uploadPlaylist(
                    name = binding.nameEditText.text.toString(),
                    songs = chosenSongs,
                    playlistImage = playlistImage,
                    success = { playlist ->
                        playlistRepository.saveOrUpdate(playlist)

                        dialogView.findViewById<TextView>(R.id.loading_message).visibility = View.GONE
                        dialogView.findViewById<ProgressBar>(R.id.loading_circle).visibility = View.GONE
                        dialogView.findViewById<TextView>(R.id.success_message).visibility = View.VISIBLE
                        dialogView.findViewById<ImageView>(R.id.success_image).visibility = View.VISIBLE

                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                dialog.dismiss()
                                findNavController().popBackStack(R.id.playlistsFragment, false)
                            }
                        , 1000)
                    },
                    fail = {
                        dialogView.findViewById<TextView>(R.id.loading_message).visibility = View.GONE
                        dialogView.findViewById<ProgressBar>(R.id.loading_circle).visibility = View.GONE
                        dialogView.findViewById<TextView>(R.id.fail_message).visibility = View.VISIBLE
                        dialogView.findViewById<ImageView>(R.id.fail_image).visibility = View.VISIBLE

                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                dialog.dismiss()
                            }
                            , 2000)
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