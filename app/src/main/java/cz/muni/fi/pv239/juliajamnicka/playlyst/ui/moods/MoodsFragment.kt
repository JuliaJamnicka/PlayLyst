package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentMoodsBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.MoodRepository

class MoodsFragment : Fragment() {
    private lateinit var binding: FragmentMoodsBinding
    private val moodRepository: MoodRepository by lazy {
        MoodRepository(requireContext())
    }
    private val adapter : MoodsAdapter by lazy {
        MoodsAdapter(
            onItemClick = { mood ->
                findNavController()
                    .navigate(MoodsFragmentDirections
                        .actionMoodsFragmentToMoodAddEditFragment(mood = mood))
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        binding.createButton.setOnClickListener {
            findNavController()
                .navigate(MoodsFragmentDirections
                    .actionMoodsFragmentToMoodAddEditFragment())
        }
    }

    private fun refreshList() {
        adapter.submitList(moodRepository.getAllMoods())
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

}