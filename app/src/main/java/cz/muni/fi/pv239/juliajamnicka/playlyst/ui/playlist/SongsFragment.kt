package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlist

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
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        binding.playlistCover.load(args.playlist?.imageLink) {
            error(R.drawable.blank_song_cover)
        }
        binding.playlistName.text = args.playlist?.name

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.deleteButton.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.alert_dialog_rounded, null)

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