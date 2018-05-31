package twitchlib.helix

import org.json.JSONObject
import twitchlib.helix.resource.*
import twitchlib.util.JsonModel
import twitchlib.util.json
import java.io.InputStream
import java.net.URL
import java.time.LocalDateTime
import javax.net.ssl.HttpsURLConnection


class HelixClient(
        private val clientId: String,
        private val clientSecret: String
) {
    private val response: TokenResponse by lazy { requestToken() }

    val clips: ClipResource = ClipResource(this)
    val games: GameResource = GameResource(this)
    val streams: StreamResource = StreamResource(this)
    val users: UserResource = UserResource(this)
    val follows: FollowResource = FollowResource(this)

//    val rateLimit: Int
//    val rateLimitRemaining: Int
//    val rateLimitReset: LocalDateTime

    fun requestAuthorized(url: URL, method: RequestMethod = RequestMethod.GET): String {
        val conn = url.openConnection() as HttpsURLConnection
        conn.setRequestProperty("Authorization", "Bearer ${response.token}")
        conn.requestMethod = method.name

        return String((conn.content as InputStream).buffered().readAllBytes())
    }

    private fun requestToken(): TokenResponse {
        val url = URL("""
            https://id.twitch.tv/oauth2/token?client_id=$clientId&client_secret=$clientSecret&grant_type=client_credentials""".trimIndent())
        val conn = url.openConnection() as HttpsURLConnection
        conn.requestMethod = "POST"

        val body = String((conn.content as InputStream).buffered().readAllBytes())
        val jObj = JSONObject(body)

        return TokenResponse(jObj)
    }
}

enum class RequestMethod {
    GET, POST
}

class TokenResponse(override val root: JSONObject) : JsonModel {
    val token: String by json("access_token")
    val expires: LocalDateTime by json("expires_in") {
        LocalDateTime.now().plusSeconds(it.toString().toLong())
    }
}