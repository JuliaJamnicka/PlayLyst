package cz.muni.fi.pv239.juliajamnicka.playlyst.api.services

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.UserInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotifyWebApiService {
    @GET("recommendations")
    fun getRecommendations(

    )

    @POST("me")
    fun getUserInfo(
    ): Call<UserInfoResponse>
}