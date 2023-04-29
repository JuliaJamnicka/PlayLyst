package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Track

data class RecommendationsResponse(
    val seeds: List<Seed>,
    val tracks : List<Track>
) {

    data class Seed(
        val afterFilteringSize: Int,
        val afterRelinkingSize: Int,
        val href: String,
        val id: String,
        val initialPoolSize: Int,
        val type: String
    )
}
