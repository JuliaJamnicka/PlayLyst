package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.util.Log
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.RetrofitUtil
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.SearchResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.services.SpotifyWebApiService
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(
    private val spotifyWebApiService: SpotifyWebApiService = RetrofitUtil.createSpotifyWebApiService()
) {
    private lateinit var accessToken: String

    fun getSearchResults(query: String, success: (List<Song>) -> Unit, fail: () -> Unit) {
        spotifyWebApiService.search(
            token = "Bearer $accessToken",
            query = query,
            type = listOf("track"),
            market = null,
            limit = 7,
            offset = 0
        ).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val searchSongs = mapResponse(responseBody)
                    success(searchSongs)
                } else {
                    Log.e(this::class.simpleName, "body was null")
                    fail()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(this::class.simpleName, t.message, t)
                fail()
            }
        })

    }

    fun updateAccessToken(token: String) {
        accessToken = token
    }

    private fun mapResponse(response: SearchResponse): List<Song> {
        return response.tracks.items.map {
            Song(
                id = 0,
                spotifyId = it.id,
                uri = it.uri,
                name = it.name,
                artist = it.artists.map { it.name }.joinToString(", "),
                genre = "", //change this laterrr
                imageLink = it.album.images[0].url
            )
        }
    }
}