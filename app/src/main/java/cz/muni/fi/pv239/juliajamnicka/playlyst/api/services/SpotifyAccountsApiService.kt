package cz.muni.fi.pv239.juliajamnicka.playlyst.api.services

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotifyAccountsApiService {
    @Headers(
        // maybe do the base64 encoding in-situ?
        // figure out how to store client id and secret. Shared params?
        "Authorization: Basic OTdkOWQ5Zjc0YzBlNDViNWExYmY1MDMyODgwMjMxN2M6YjU0N2FmNDg4YjU2NDUyNmEwZTA2Y2U3YjFhMjk0NzQ=",
        "Content-Type: application/x-www-form-urlencoded"
    )
    @POST("token")
    fun getAccessToken(
        @Query("code") code: String,
        @Query("redirect_uri") redirect_uri: String,
        @Query("grant_type") grant_type: String = "authorization_code"
    ): Call<AccessTokenResponse>

    @Headers(
        // maybe do the base64 encoding in-situ?
        // figure out how to store client id and secret. Shared params?
        "Authorization: Basic OTdkOWQ5Zjc0YzBlNDViNWExYmY1MDMyODgwMjMxN2M6YjU0N2FmNDg4YjU2NDUyNmEwZTA2Y2U3YjFhMjk0NzQ=",
        "Content-Type: application/x-www-form-urlencoded"
    )
    @POST("token")
    fun getRefreshedToken(
        @Query("grant_type") grant_type: String = "refresh_token",
        @Query("refresh_token") refresh_token: String
    ): Call<AccessTokenResponse>
}