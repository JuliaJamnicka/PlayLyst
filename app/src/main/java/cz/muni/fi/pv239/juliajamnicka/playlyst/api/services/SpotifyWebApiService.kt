package cz.muni.fi.pv239.juliajamnicka.playlyst.api.services

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.NewPlaylistBody
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.RecommendationsQueries
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.*
import retrofit2.Call
import retrofit2.http.*

interface SpotifyWebApiService {
    @GET("recommendations")
    fun getRecommendations(
        @Header("Authorization") token: String,
        @QueryMap queries: Map<String, @JvmSuppressWildcards Any>
    ) : Call<RecommendationsResponse>

    @GET("me")
    fun getCurrentUsersProfile(
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

    @GET("/v1/recommendations/available-genre-seeds") //why v1 manually needed? find out
    fun getGenreSeeds(
        @Header("Authorization") token: String,
    ): Call<GenreSeedResponse>

    @POST("/users/{user_id}/playlists")
    fun createPlaylist(
        @Path("user_id") userId: String,
        @Body Body: NewPlaylistBody
    ) : Call<CreatePlaylistResponse>

    @PUT("/playlists/{playlist_id}/images")
    fun addCustomPlaylistCoverImage(
        @Path("playlist_id") playlistId: String,
        @Body Body: String
    )

    @POST("playlists/{playlist_id}/tracks")
    fun addItemsToPlaylist(
        @Path("playlist_id") playlistId: String,
        @Query("uris") uris: String
    ): String
}