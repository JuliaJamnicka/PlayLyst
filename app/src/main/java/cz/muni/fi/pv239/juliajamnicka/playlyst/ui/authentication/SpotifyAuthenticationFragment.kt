package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSpotifyAuthenticationBinding

class SpotifyAuthenticationFragment : Fragment() {
    private val REQUEST_CODE = 1330
    private val CLIENT_ID = "97d9d9f74c0e45b5a1bf50328802317c"
    private val REDIRECT_URI = "cz.muni.fi.pv239.juliajamnicka.playlyst://callback"

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val response = AuthorizationClient.getResponse(it.resultCode, it.data)

            if (response.type === AuthorizationResponse.Type.TOKEN) {
                val token = response.accessToken
                val expiresIn = response.expiresIn
            }
        }
    }

    private lateinit var binding: FragmentSpotifyAuthenticationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSpotifyAuthenticationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val intent = createSpotifyAuthenticationIntent()
            launcher.launch(intent)
        }

    }

    private fun createSpotifyAuthenticationIntent(): Intent {
        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming", "ugc-image-upload", "app-remote-control",
            "playlist-modify-private", "playlist-modify-public"))
        val request = builder.build()


        return AuthorizationClient.createLoginActivityIntent(activity, request)
    }


}