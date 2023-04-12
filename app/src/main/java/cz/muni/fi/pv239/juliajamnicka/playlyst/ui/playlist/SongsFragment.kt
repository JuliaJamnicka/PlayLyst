package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSongsBinding

class SongsFragment : Fragment() {
    private lateinit var binding: FragmentSongsBinding

    private val adapter: SongsAdapter by lazy {
        SongsAdapter(
            onItemClick = { song ->
                {}
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

    }

    // TODO refresh and resume to add later

}