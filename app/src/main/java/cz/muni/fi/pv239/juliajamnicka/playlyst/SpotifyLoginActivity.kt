package cz.muni.fi.pv239.juliajamnicka.playlyst

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ActivityMainBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.ui.authentication.SpotifyAuthorizationFragment

class SpotifyLoginActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_login)

        supportFragmentManager
            .setFragmentResultListener("request_code", this) { _, bundle ->
                val code = bundle.getString("response_code")

                val preferences = getPreferences(Context.MODE_PRIVATE)
                with (preferences.edit()) {
                    putString("spotify_authorization_code", code)
                    apply()
                }

                Log.d("SPOT_AUTH_CODE", preferences.getString("spotify_authorization_code", "")!!)
            }
    }
}