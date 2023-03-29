package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val expires_in: Int,
    val refresh_token: String
)
