package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlist

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.Coil
import coil.request.ImageRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSongsBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.PlaylistRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.isLight


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
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongsBinding.inflate(layoutInflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPlaylistCover()

        binding.playlistName.text = args.playlist?.name

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.spotifyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(args.playlist!!.uri))
            intent.setPackage("com.spotify.music") // Specify the package name of the Spotify app

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                // TODO: dialog saying that Spotify is not installed
            }
        }

        binding.deleteButton.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.playlist_delete_alert_dialog, null)

            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded).apply {
                setView(dialogView)
            }.create()

            dialogView.findViewById<Button>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.positive_button).setOnClickListener {
                playlistRepository.deletePlaylist(args.playlist!!)
                findNavController().navigateUp()
                dialog.cancel()
            }
            dialog.show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        refreshList()

    }

    private fun loadPlaylistCover() {
        val imageLoader = Coil.imageLoader(requireContext())

        val target = object : coil.target.Target {
            override fun onSuccess(result: Drawable) {
                binding.playlistCover.setImageDrawable(result)
                if (result is BitmapDrawable) {
                    // TODO: add the 75% opacity to make results more accurate
                    determineAppBarTextColor(result.bitmap)
                }
            }
        }

        val request = ImageRequest.Builder(requireContext())
            .data(args.playlist?.imageLink)
            .target(target)
            .allowHardware(false)
            .build()

        imageLoader.enqueue(request)
    }

    private fun determineAppBarTextColor(cover: Bitmap) {
        val color = ColorStateList.valueOf(if (cover.isLight()) Color.BLACK else Color.WHITE)

        binding.playlistName.setTextColor(color)
        binding.backButton.imageTintList = color
        binding.spotifyButton.imageTintList = color
        binding.deleteButton.imageTintList = color
    }

    private fun refreshList() {
        adapter.submitList(args.playlist?.songs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.show()
    }

}