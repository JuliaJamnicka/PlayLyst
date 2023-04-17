package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentPlaylistAddEditBinding

class PlaylistAddEditFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistAddEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistAddEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}