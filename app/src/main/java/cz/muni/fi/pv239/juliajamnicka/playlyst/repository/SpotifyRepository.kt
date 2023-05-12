package cz.muni.fi.pv239.juliajamnicka.playlyst.repository

import android.graphics.Bitmap
import android.util.Log
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.RetrofitUtil
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.SessionManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.UpdateSongsBody
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.NewPlaylistBody
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Image
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Track
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.services.SpotifyWebApiService
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.encodeToBase64String
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpotifyRepository(
    private val spotifyWebApiService: SpotifyWebApiService = RetrofitUtil.createSpotifyWebApiService()
) {
    fun getSearchResults(query: String, success: (List<Song>) -> Unit, fail: () -> Unit) {
        spotifyWebApiService.search(
            token = "Bearer ${SessionManager.getToken("access_token")}",
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
            token = "Bearer ${SessionManager.getToken("access_token")}"
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
                    queries[attrName] = if (attribute.isFloat)
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
            queries["limit"] = 20

            return queries
        }

        spotifyWebApiService.getRecommendations(
            token = "Bearer ${SessionManager.getToken("access_token")}",
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

    fun uploadPlaylist(name: String, songs: List<Song>, playlistImage: Bitmap?,
                       success: (Playlist) -> Unit, fail: () -> Unit) {

        // TODO surely this can be done better with retrofit? look into it
        fun getPlaylistCover(playlist: CreatePlaylistResponse) {
            spotifyWebApiService.getPlaylistCoverImage(
                token = "Bearer ${SessionManager.getToken("access_token")}",
                playlistId = playlist.id
            ).enqueue(object : Callback<List<Image>> {
                override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        val imageLink = responseBody.first().url
                        success(mapCreatePlaylistResponseToPlaylist(playlist, imageLink, songs))
                    } else {
                        Log.e(this::class.simpleName, "body was null")
                        fail()
                    }
                }

                override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                    Log.e(this::class.simpleName, t.message, t)
                    fail()
                }
            })
        }

        fun addSongsToPlaylist(playlist: CreatePlaylistResponse) {
            spotifyWebApiService.addItemsToPlaylist(
                token = "Bearer ${SessionManager.getToken("access_token")}",
                playlistId = playlist.id,
                body = UpdateSongsBody(
                    uris = songs.map{ it.uri }
                )
            ).enqueue(object : Callback<UpdatePlaylistItemsResponse> {
                override fun onResponse(call: Call<UpdatePlaylistItemsResponse>,
                                        response: Response<UpdatePlaylistItemsResponse>) {
                    if (response.isSuccessful) {
                        getPlaylistCover(playlist)
                    } else {
                        Log.e(this::class.simpleName, "body was null")
                        fail()
                    }
                }

                override fun onFailure(call: Call<UpdatePlaylistItemsResponse>, t: Throwable) {
                    Log.e(this::class.simpleName, t.message, t)
                    fail()
                }
            })
        }

        fun addCoverToPlaylist(playlist: CreatePlaylistResponse) {
            val requestBody = playlistImage!!.encodeToBase64String()
                .toRequestBody("image/jpeg".toMediaTypeOrNull())
            spotifyWebApiService.addCustomPlaylistCoverImage(
                token = "Bearer ${SessionManager.getToken("access_token")}",
                playlistId = playlist.id,
                body = requestBody
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        addSongsToPlaylist(playlist)
                    } else {
                        Log.e(this::class.simpleName, "body was null")
                        fail()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e(this::class.simpleName, t.message, t)
                    fail()
                }
            })
        }

        fun createPlaylist(userId: String) {
            spotifyWebApiService.createPlaylist(
                token = "Bearer ${SessionManager.getToken("access_token")}",
                userId = userId,
                body = NewPlaylistBody(
                    name = name
                )
            ).enqueue(object : Callback<CreatePlaylistResponse> {
                override fun onResponse(call: Call<CreatePlaylistResponse>, response: Response<CreatePlaylistResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        if (playlistImage != null) {
                            addCoverToPlaylist(responseBody)
                        } else {
                            addSongsToPlaylist(responseBody)
                        }
                    } else {
                        Log.e(this::class.simpleName, "body was null")
                        fail()
                    }
                }

                override fun onFailure(call: Call<CreatePlaylistResponse>, t: Throwable) {
                    Log.e(this::class.simpleName, t.message, t)
                    fail()
                }
            })
        }

        fun getUserInfo() {
            spotifyWebApiService.getCurrentUsersProfile(
                token = "Bearer ${SessionManager.getToken("access_token")}",
            ).enqueue(object : Callback<UserInfoResponse> {
                    override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody != null) {
                            createPlaylist(responseBody.id)
                        } else {
                            Log.e(this::class.simpleName, "body was null")
                            fail()
                        }
                    }

                    override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                        Log.e(this::class.simpleName, t.message, t)
                        fail()
                    }
                })
        }

        getUserInfo()
    }

    fun mapCreatePlaylistResponseToPlaylist(
        playlist: CreatePlaylistResponse,
        imageLink: String,
        songs: List<Song>
    ): Playlist {
        return Playlist(
            id = 0,
            spotifyId = playlist.id,
            uri = playlist.uri,
            name = playlist.name,
            imageLink = imageLink,
            songs = songs
        )
    }

    fun deletePlaylist(playlistId: String, success: () -> Unit, fail: () -> Unit) {
        spotifyWebApiService.deletePlaylist(
            token = "Bearer ${SessionManager.getToken("access_token")}",
            playlistId = playlistId,
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    success()
                } else {
                    Log.e(this::class.simpleName, "playlist not deleted")
                    fail()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(this::class.simpleName, t.message, t)
                fail()
            }
        })
    }

    fun deleteSongsFromPlaylist(playlistId: String, songs: List<Song>,
                                success: () -> Unit, fail: () -> Unit) {
        spotifyWebApiService.addItemsToPlaylist(
            token = "Bearer ${SessionManager.getToken("access_token")}",
            playlistId = playlistId,
            body = UpdateSongsBody(
                uris = songs.map{ it.uri }
            )
        ).enqueue(object : Callback<UpdatePlaylistItemsResponse> {
            override fun onResponse(call: Call<UpdatePlaylistItemsResponse>,
                                    response: Response<UpdatePlaylistItemsResponse>) {
                if (response.isSuccessful) {
                    success()
                } else {
                    Log.e(this::class.simpleName, "songs could not be deleted from playlist")
                    fail()
                }
            }

            override fun onFailure(call: Call<UpdatePlaylistItemsResponse>, t: Throwable) {
                Log.e(this::class.simpleName, t.message, t)
                fail()
            }
        })
    }


}