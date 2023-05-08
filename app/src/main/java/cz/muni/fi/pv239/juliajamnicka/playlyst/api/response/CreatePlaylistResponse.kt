package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Image

data class CreatePlaylistResponse(
    val id: String,
    val uri: String,
    val href: String,
    val images: List<Image>,
    val name: String,
    val description: String,
    val public: Boolean,
    val tracks: SearchResponse.Tracks
)
