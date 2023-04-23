package cz.muni.fi.pv239.juliajamnicka.playlyst.api.services

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.GenreSeedResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.SearchResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.UserInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotifyWebApiService {
    @GET("recommendations")
    fun getRecommendations(

    )

    @POST("me")
    fun getUserInfo(
    ): Call<UserInfoResponse>

    @GET("search")
    fun search(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: List<String>,
        @Query("market") market: String?,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
    ): Call<SearchResponse>

    @GET("/recommendations/available-genre-seeds")
    fun getGenreSeeds(
        @Header("Authorization") token: String,
    ): Call<GenreSeedResponse>
}