package cz.muni.fi.pv239.juliajamnicka.playlyst.api

import retrofit2.http.GET

interface SpotifyWebApiService {
    @GET("recommendations")
    fun getRecommendations(

    )
}