package cz.muni.fi.pv239.juliajamnicka.playlyst.api

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.*


class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val token = getUpdatedToken()) {
                token -> {
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                }
                else -> null
            }
        }
    }

    private fun getUpdatedToken(): String? {
        var newToken: String? = null

        if (SessionManager.getToken("refresh_token") == null) {
            SessionManager.getAccessToken(
                success = { accessToken -> newToken = accessToken },
                fail = { Log.e(this::class::simpleName.toString(), "did not work") })
        } else {
            SessionManager.refreshToken(
                success = { accessToken -> newToken = accessToken },
                fail = { Log.e(this::class::simpleName.toString(), "did not work") })
        }

        return newToken
    }

}