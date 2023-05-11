package cz.muni.fi.pv239.juliajamnicka.playlyst.api

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.*


class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        var newToken: String? = null

        // TODO: asynchronous magic needs fix; return something maybe?
        runBlocking {
            if (SessionManager.getToken("refresh_token") == null) {
                SessionManager.getAccessToken(
                    success = { accessToken -> newToken = accessToken },
                    fail = { Log.e(this::class::simpleName.toString(), "did not work") })
            } else {
                SessionManager.refreshToken(
                    success = { accessToken -> newToken = accessToken },
                    fail = { Log.e(this::class::simpleName.toString(), "did not work") })
            }

        }

        if (newToken != null) {
            SessionManager.saveToken("access_token", newToken!!)
            return response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
        return null
    }
}