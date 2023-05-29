package cz.muni.fi.pv239.juliajamnicka.playlyst.api.services

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.UpdateSongsBody
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.NewPlaylistBody
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.query.PlayBody
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Image
import okhttp3.RequestBody
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
        @Header("Authorization") token: String,
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

    @POST("users/{user_id}/playlists")
    fun createPlaylist(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String,
        @Body body: NewPlaylistBody
    ) : Call<CreatePlaylistResponse>

    @PUT("playlists/{playlist_id}/images")
    fun addCustomPlaylistCoverImage(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String,
        @Body body: RequestBody
    ): Call<Void>

    @GET("playlists/{playlist_id}/images")
    fun getPlaylistCoverImage(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String
    ): Call<List<Image>>

    @POST("playlists/{playlist_id}/tracks")
    fun addItemsToPlaylist(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String,
        @Body body: UpdateSongsBody
    ): Call<UpdatePlaylistItemsResponse>

    @DELETE("playlists/{playlist_id}/tracks")
    fun deletePlaylistItems(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String,
        @Body body: UpdateSongsBody
    ): Call<UpdatePlaylistItemsResponse>

    @DELETE("playlists/{playlist_id}/followers")
    fun deletePlaylist(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String
    ) : Call<Void>

    @GET("me/player/devices")
    fun getAvailableDevices(
        @Header("Authorization") token: String,
    ): Call<AvailableDevicesResponse>

    @PUT("me/player/play")
    fun playSong(
        @Header("Authorization") token: String,
        @Query("device_id") deviceId: String,
        @Body body: PlayBody
    ): Call<Void>

    @PUT("me/player/pause")
    fun pauseSong(
        @Header("Authorization") token: String,
    ): Call<Void>
}