package cz.muni.fi.pv239.juliajamnicka.playlyst.api.query

data class RecommendationsQueries(
    val limit: Int,
    val market: String? = null,
    val seedTracks: String? = null,
    val seedGenres: String? = null,
    val seedArtists: String? = null,

    val minAcousticness: Float? = null,
    val maxAcousticness: Float? = null,
    val targetAcousticness: Float? = null,
    val minDanceability: Float? = null,
    val maxDanceability: Float? = null,
    val targetDanceability: Float? = null,
    val minEnergy: Float? = null,
    val maxEnergy: Float? = null,
    val targetEnergy: Float? = null,
    val minInstrumentalness: Float? = null,
    val maxInstrumentalness: Float? = null,
    val targetInstrumentalness: Float? = null,
    val minKey: Int? = null,
    val maxKey: Int? = null,
    val targetKey: Int? = null,
    val minLiveness: Float? = null,
    val maxLiveness: Float? = null,
    val targetLiveness: Float? = null,
    val minMode: Int? = null,
    val maxMode: Int? = null,
    val targetMode: Int? = null,
    val minPopularity: Int? = null,
    val maxPopularity: Int? = null,
    val targetPopularity: Int? = null,
    val minTempo: Float? = null,
    val maxTempo: Float? = null,
    val targetTempo: Float? = null,
    val minValence: Float? = null,
    val maxValence: Float? = null,
    val targetValence: Float? = null
) {
    companion object {
        fun mapToObject(map: Map<String, Any?>) : RecommendationsQueries {
            val constructor = RecommendationsQueries::class.constructors.first()

            val args = constructor
                .parameters.associateWith { map[it.name] }

            return constructor.callBy(args)
        }
    }
}
