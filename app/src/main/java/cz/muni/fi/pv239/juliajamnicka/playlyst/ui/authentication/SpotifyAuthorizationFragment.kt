package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.RetrofitUtil.createSpotifyAccountsApiService
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.AccessTokenResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentSpotifyAuthorizationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpotifyAuthorizationFragment : Fragment() {
    private val CLIENT_ID = "97d9d9f74c0e45b5a1bf50328802317c"
    private val REDIRECT_URI = "cz.muni.fi.pv239.juliajamnicka.playlyst://callback"

    private lateinit var preferences: SharedPreferences

    private lateinit var binding: FragmentSpotifyAuthorizationBinding

    //just temporary to not make Spotify sus of the repeated calls
    private var code: String = ""

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val response = AuthorizationClient.getResponse(it.resultCode, it.data)

            if (response.type === AuthorizationResponse.Type.CODE) {
                code = response.code
            }

            with (preferences.edit()) {
                putString("spotify_authorization_code", code)
                apply()
            }

            // should run in the background
            getAccessToken(success = { accessTokenResponse ->
                with (preferences.edit()) {
                    putString("token", accessTokenResponse.access_token)
                    putString("refresh-token", accessTokenResponse.refresh_token)
                    apply()
                    // surely not like this, expires_in also needs to be saved and checked
                    // but that's a problem for future me
                    Toast.makeText(context, "Access token received", Toast.LENGTH_SHORT).show()
                }
            }, fail = {
                Toast.makeText(context, "Access token error", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSpotifyAuthorizationBinding.inflate(layoutInflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        //mainActivity.setBottomNavigationVisibility(View.GONE)
        mainActivity.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = this.requireActivity().getPreferences(Context.MODE_PRIVATE)

        //code = preferences.getString("spotify_authorization_code", null) ?: ""

        binding.loginButton.setOnClickListener {
            if (code.isEmpty()) {
                val intent = createSpotifyAuthenticationIntent()
                launcher.launch(intent)
            }

            // this kills my background thread :/ move the whole thing to mainActivity?
            //findNavController().navigate(SpotifyAuthorizationFragmentDirections
            //        .actionSpotifyAuthorizationFragmentToPlaylistsFragment())
        }

    }

    private fun createSpotifyAuthenticationIntent(): Intent {
        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming", "ugc-image-upload", "app-remote-control",
            "playlist-modify-private", "playlist-modify-public"))
        //builder.setShowDialog(true)
        // setting a state is recommended, maybe add later?
        val request = builder.build()

        return AuthorizationClient.createLoginActivityIntent(activity, request)
    }

    private fun getAccessToken(success: (AccessTokenResponse) -> Unit, fail: () -> Unit) {
        val service = createSpotifyAccountsApiService()

        val result = service.getAccessToken(code, REDIRECT_URI)
            .enqueue(object : Callback<AccessTokenResponse> {
                override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        Log.e(this::class.simpleName, "returned ok")
                        success(responseBody)
                    } else {
                        Log.e(this::class.simpleName, "body was null")
                        fail()
                    }
                }

                override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                    Log.e(this::class.simpleName, t.message, t)
                    fail()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.show()
    }
}