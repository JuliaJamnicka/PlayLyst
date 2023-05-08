package cz.muni.fi.pv239.juliajamnicka.playlyst.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import cz.muni.fi.pv239.juliajamnicka.playlyst.BuildConfig
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.AccessTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SessionManager {
    private lateinit var sharedPreferences: SharedPreferences

    private val spotifyAccountsApiService = RetrofitUtil.createSpotifyAccountsApiService()

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("PlayLyst.prefs", Context.MODE_PRIVATE)
    }

    fun saveToken(key: String, value: String) {
        with (sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun getToken(key: String): String? =
        sharedPreferences.getString(key, null)

    fun getAccessToken(success: (String) -> Unit, fail: () -> Unit) {
        spotifyAccountsApiService.getAccessToken(
            code = getToken("authorization_code")!!,
            redirect_uri = BuildConfig.REDIRECT_URI
        ).enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.e(this::class.simpleName, "returned ok")
                    saveToken("access_token", responseBody.access_token)
                    saveToken("refresh_token", responseBody.refresh_token!!)
                    success(responseBody.access_token)
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

    fun refreshToken(success: (String) -> Unit, fail: () -> Unit) {
        val token = getToken("refresh_token") ?: ""

        spotifyAccountsApiService.getRefreshedToken(
            refresh_token = token
        ).enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    saveToken("access_token", responseBody.access_token)
                    success(responseBody.access_token)
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
}