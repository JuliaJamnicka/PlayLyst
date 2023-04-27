package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentCreatePlaylistAddMoodBottomSheetBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.MoodRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods.MoodsAdapter


class CreatePlaylistAddMoodBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreatePlaylistAddMoodBottomSheetBinding

    private val moodRepository: MoodRepository by lazy {
        MoodRepository(requireContext())
    }

    private val adapter : MoodsAdapter by lazy {
        MoodsAdapter(
            onItemClick = { mood ->
                val navController = findNavController()
                navController.previousBackStackEntry?.savedStateHandle?.set("mood", mood)
                navController.popBackStack()
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreatePlaylistAddMoodBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        refreshList()

    }

    private fun refreshList() {
        adapter.submitList(moodRepository.getAllMoods())
    }

}