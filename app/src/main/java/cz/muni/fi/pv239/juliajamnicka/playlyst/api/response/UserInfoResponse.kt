package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

import cz.muni.fi.pv239.juliajamnicka.playlyst.api.response.data.Image

data class UserInfoResponse(
    val display_name: String,
    val href: String,
    val id: String,
    val images: List<Image>,
    val uri: String
)
