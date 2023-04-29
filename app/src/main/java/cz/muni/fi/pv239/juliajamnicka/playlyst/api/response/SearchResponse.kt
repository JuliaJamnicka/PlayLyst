package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Track

data class SearchResponse(
    val tracks: Tracks
) {

    data class Tracks(
        val href: String,
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int,
        val items: List<Track>
    )
}
