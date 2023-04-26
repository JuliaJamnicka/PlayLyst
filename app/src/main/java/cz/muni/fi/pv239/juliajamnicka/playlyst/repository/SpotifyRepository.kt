package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.util.Log
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.RetrofitUtil
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.GenreSeedResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.SearchResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.services.SpotifyWebApiService
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Artist
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpotifyRepository(
    private val spotifyWebApiService: SpotifyWebApiService = RetrofitUtil.createSpotifyWebApiService()
) {
    private lateinit var accessToken: String

    fun getSearchResults(query: String, success: (List<Song>) -> Unit, fail: () -> Unit) {
        spotifyWebApiService.search(
            token = "Bearer $accessToken",
            query = query,
            type = listOf("track"),
            market = null,
            limit = 15,
            offset = 0
        ).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val searchSongs = mapSearchResponse(responseBody)
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

    private fun mapSearchResponse(response: SearchResponse): List<Song> {
        return response.tracks.items.map {
            Song(
                id = 0,
                spotifyId = it.id,
                uri = it.uri,
                name = it.name,
                genre = "", //change this laterrr
                imageLink = it.album.images[0].url,
                artists = it.artists.map {
                    Artist(
                        id = 0,
                        spotifyId = it.id,
                        name = it.name,
                        uri = it.uri,
                        imageLink = null
                    )
                }
            )
        }
    }

    fun getGenreSeeds(success: (List<String>) -> Unit, fail: () -> Unit) {
        spotifyWebApiService.getGenreSeeds(
            token = "Bearer $accessToken"
        ).enqueue(object : Callback<GenreSeedResponse> {
            override fun onResponse(call: Call<GenreSeedResponse>, response: Response<GenreSeedResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody.genres)
                } else {
                    Log.e(this::class.simpleName, "body was null")
                    fail()
                }
            }

            override fun onFailure(call: Call<GenreSeedResponse>, t: Throwable) {
                Log.e(this::class.simpleName, t.message, t)
                fail()
            }
        })
    }

}