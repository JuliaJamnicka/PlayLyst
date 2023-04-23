package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

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
    ) {

        data class Track(
            val album: Album,
            val artists: List<Artist>,
            val available_markets: List<String>,
            val href: String,
            val id: String,
            val name: String,
            val popularity: Int,
            val uri: String,
            val is_local: Boolean
        )

        data class Album(
            val album_type: String,
            val total_tracks: Int,
            val available_markets: List<String>,
            val href: String,
            val images: List<Image>,
            val name: String,
            val genres: List<String>
        )

        data class Artist(
            val href: String,
            val id: String,
            val name: String,
            val uri: String
        )

        data class Image(
            val height: Int,
            val width: Int,
            val url: String
        )
    }
}
