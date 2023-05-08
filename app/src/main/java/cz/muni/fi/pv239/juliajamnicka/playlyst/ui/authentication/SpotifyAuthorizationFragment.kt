package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.SessionManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSpotifyAuthorizationBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.BuildConfig


class SpotifyAuthorizationFragment : Fragment() {
    private lateinit var binding: FragmentSpotifyAuthorizationBinding

    private var code: String = ""

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val response = AuthorizationClient.getResponse(it.resultCode, it.data)

            if (response.type === AuthorizationResponse.Type.CODE) {
                code = response.code
                SessionManager.saveToken("authorization_code", code)

                SessionManager.getAccessToken(
                    success = {},
                    fail = {
                        Toast.makeText(context, "imma die of sadness", Toast.LENGTH_SHORT).show()})
            }
            findNavController().navigate(SpotifyAuthorizationFragmentDirections
                .actionSpotifyAuthorizationFragmentToPlaylistsFragment())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSpotifyAuthorizationBinding.inflate(layoutInflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.GONE)
        mainActivity.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            if (code.isEmpty()) {
                val intent = createSpotifyAuthenticationIntent()
                launcher.launch(intent)
            }
        }
    }

    private fun createSpotifyAuthenticationIntent(): Intent {
        val builder =
            AuthorizationRequest.Builder(
                BuildConfig.SPOTIFY_CLIENT_ID,
                AuthorizationResponse.Type.CODE,
                BuildConfig.REDIRECT_URI)

        builder.setScopes(arrayOf("streaming", "ugc-image-upload", "app-remote-control",
            "playlist-modify-private", "playlist-modify-public", "user-read-private",
            "user-read-email"))
        //builder.setShowDialog(true)
        // setting a state is recommended, maybe add later?
        val request = builder.build()

        return AuthorizationClient.createLoginActivityIntent(activity, request)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.show()
    }
}