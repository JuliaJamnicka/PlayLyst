package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Mood
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentMoodAddEditBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.MoodRepository


class MoodAddEditFragment : Fragment() {
    private lateinit var binding: FragmentMoodAddEditBinding

    private val moodRepository: MoodRepository by lazy {
        MoodRepository(requireContext())
    }

    private val adapter : MoodAttributesAdapter by lazy {
        MoodAttributesAdapter(
            onItemClick = { moodAttribute -> {}
            }
        )
    }
    private val args: MoodAddEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodAddEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        setInitialValues()

    }

    fun refreshList() {
        adapter.submitList(args.mood?.attributes)
    }

    private fun setInitialValues() {
        if (args.mood != null) {

        }
    }


}