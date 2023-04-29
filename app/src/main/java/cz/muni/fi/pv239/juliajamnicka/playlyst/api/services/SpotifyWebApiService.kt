package cz.muni.fi.pv239.juliajamnicka.playlyst.api.services

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.GenreSeedResponse
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.RecommendationsResponse
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
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("market") market: String?,
        // at least one of the seeds is always required
        @Query("seed_tracks") seedTracks: String? = null,
        @Query("seed_artists") seedArtists: String? = null,
        @Query("seed_genres") seedGenres: String? = null,
        @Query("min_acousticness") minAcousticness: Float? = null,
        @Query("max_acousticness") maxAcousticness: Float? = null,
        @Query("target_acousticness") targetAcousticness: Float? = null,
        @Query("min_danceability") minDanceability: Float? = null,
        @Query("max_danceability") maxDanceability: Float? = null,
        @Query("target_danceability") targetDanceability: Float? = null,
        @Query("min_duration_ms") minDurationMs: Int? = null,
        @Query("max_duration_ms") maxDurationMs: Int? = null,
        @Query("target_duration_ms") targetDurationMs: Int? = null,
        @Query("min_energy") minEnergy: Float? = null,
        @Query("max_energy") maxEnergy: Float? = null,
        @Query("target_energy") targetEnergy: Float? = null,
        @Query("min_instrumentalness") minInstrumentalness: Float? = null,
        @Query("max_instrumentalness") maxInstrumentalness: Float? = null,
        @Query("target_instrumentalness") targetInstrumentalness: Float? = null,
        @Query("min_key") minKey: Int? = null,
        @Query("max_key") maxKey: Int? = null,
        @Query("target_key") targetKey: Int? = null,
        @Query("min_liveness") minLiveness: Float? = null,
        @Query("max_liveness") maxLiveness: Float? = null,
        @Query("target_liveness") targetLiveness: Float? = null,
        @Query("min_loudness") minLoudness: Float? = null,
        @Query("max_loudness") maxLoudness: Float? = null,
        @Query("target_loudness") targetLoudness: Float? = null,
        @Query("min_mode") minMode: Int? = null,
        @Query("max_mode") maxMode: Int? = null,
        @Query("target_mode") targetMode: Int? = null,
        @Query("min_popularity") minPopularity: Int? = null,
        @Query("max_popularity") maxPopularity: Int? = null,
        @Query("target_popularity") targetPopularity: Int? = null,
        @Query("min_speechiness") minSpeechiness: Float? = null,
        @Query("max_speechiness") maxSpeechiness: Float? = null,
        @Query("target_speechiness") targetSpeechiness: Float? = null,
        @Query("min_tempo") minTempo: Float? = null,
        @Query("max_tempo") maxTempo: Float? = null,
        @Query("target_tempo") targetTempo: Float? = null,
        @Query("min_time_signature") minTimeSignature: Int? = null,
        @Query("max_time_signature") maxTimeSignature: Int? = null,
        @Query("target_time_signature") targetTimeSignature: Int? = null,
        @Query("min_valence") minValence: Float? = null,
        @Query("maxValence") maxValence: Float? = null,
        @Query("targetValence") targetValence: Float? = null
    ) : Call<RecommendationsResponse>

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

    @GET("/v1/recommendations/available-genre-seeds")
    fun getGenreSeeds(
        @Header("Authorization") token: String,
    ): Call<GenreSeedResponse>
}