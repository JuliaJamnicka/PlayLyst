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
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSpotifyAuthorizationBinding

class SpotifyAuthorizationFragment : Fragment() {
    private val CLIENT_ID = "97d9d9f74c0e45b5a1bf50328802317c"
    private val REDIRECT_URI = "cz.muni.fi.pv239.juliajamnicka.playlyst://callback"

    private lateinit var binding: FragmentSpotifyAuthorizationBinding

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val response = AuthorizationClient.getResponse(it.resultCode, it.data)

            if (response.type === AuthorizationResponse.Type.CODE) {
                val code = response.code
                //return this to higher activity
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSpotifyAuthorizationBinding.inflate(layoutInflater, container, false)
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
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming", "ugc-image-upload", "app-remote-control",
            "playlist-modify-private", "playlist-modify-public"))
        builder.setShowDialog(true)
        // setting a state is recommended, maybe add later?
        val request = builder.build()

        return AuthorizationClient.createLoginActivityIntent(activity, request)
    }
}