package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.graphics.Bitmap
import android.util.Log
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.RetrofitUtil
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.GenreSeedResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.RecommendationsResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.SearchResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Track
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.services.SpotifyWebApiService
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.*
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
        return mapTracksToSongs(response.tracks.items)
    }

    private fun mapTracksToSongs(tracks: List<Track>): List<Song> {
        return tracks.map {
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

    fun getRecommendations(songs: List<Song>, genres: List<String>, attributes: List<MoodAttribute>,
                           success: (Pair<List<SeedInfo>, List<Song>>) -> Unit, fail: () -> Unit) {
        fun createRecommendationsQueries(): Map<String, Any> {
            val queries: MutableMap<String, Any> = mutableMapOf()

            for (attribute in attributes) {
                val name = attribute.name.lowercase()
                for ((attrName, value) in listOf(
                    Pair("target_$name", attribute.value),
                    Pair("min_$name", attribute.lowerValue),
                    Pair("max_$name", attribute.upperValue)
                )) {
                    if (value == null) {
                        continue
                    }
                    queries[attrName] = if (attribute.canHaveRange)
                        value.toFloat()
                    else value.toInt()
                }
            }

            val seedSongIds = songs.joinToString(",") {
                it.spotifyId
            }
            val seedGenres = genres.joinToString(",")

            queries["seed_tracks"] = seedSongIds
            queries["seed_genres"] = seedGenres
            queries["limit"] = 50

            return queries
        }

        spotifyWebApiService.getRecommendations(
            token = "Bearer $accessToken",
            queries = createRecommendationsQueries()
        ).enqueue(object : Callback<RecommendationsResponse> {
            override fun onResponse(call: Call<RecommendationsResponse>,
                                    response: Response<RecommendationsResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(mapRecommendationsResponse(responseBody))
                } else {
                    Log.e(this::class.simpleName, "body was null")
                    fail()
                }
            }

            override fun onFailure(call: Call<RecommendationsResponse>, t: Throwable) {
                Log.e(this::class.simpleName, t.message, t)
                fail()
            }
        })
    }

    private fun mapRecommendationsResponse(response: RecommendationsResponse)
    : Pair<List<SeedInfo>, List<Song>> {
        val seedsInfos = response.seeds.map {
            SeedInfo(
                seedId = it.id,
                afterFilteringSize = it.afterFilteringSize,
                initialPoolSize = it.initialPoolSize
            )
        }

        return Pair(seedsInfos, mapTracksToSongs(response.tracks))
    }

    fun createPlaylist(name: String, songs: List<Song>, playlistImage: Bitmap?,
                       success: (Playlist) -> Unit, failure: () -> Unit) {

    }

    private fun getUserInfo(): String {
        spotifyWebApiService.getCurrentUsersProfile()

        return ""
    }
}