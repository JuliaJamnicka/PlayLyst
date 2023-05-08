package cz.muni.fi.pv239.juliajamnicka.playlyst.api

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.services.SpotifyAccountsApiService
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.services.SpotifyWebApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitUtil {
    private const val WEB_API_BASE_URL = "https://api.spotify.com/v1/"
    private const val ACCOUNTS_BASE_URL = "https://accounts.spotify.com/api/"

    fun createSpotifyWebApiService(): SpotifyWebApiService =
        create(WEB_API_BASE_URL, createOkHttpClient())

    fun createSpotifyAccountsApiService(): SpotifyAccountsApiService =
        create(ACCOUNTS_BASE_URL, createOkHttpClient())

    private inline fun <reified T> create(baseUrl: String, okHttpClient: OkHttpClient): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(T::class.java)

    private fun createOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            addInterceptor(logging)
            authenticator(TokenAuthenticator())
        }.build()
}