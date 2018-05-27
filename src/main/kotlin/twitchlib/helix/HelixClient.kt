package twitchlib.helix

import org.json.JSONObject
import twitchlib.helix.resource.*
import java.io.InputStream
import java.net.URL
import java.time.LocalDateTime
import javax.net.ssl.HttpsURLConnection


class HelixClient(
        private val clientId: String,
        private val clientSecret: String
) {
    private val token: Pair<String, LocalDateTime> by lazy { requestToken() }

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
        conn.setRequestProperty("Authorization", "Bearer ${token.first}")
        conn.requestMethod = method.name

        return String((conn.content as InputStream).buffered().readAllBytes())
    }

    private fun requestToken(): Pair<String, LocalDateTime> {
        val url = URL("""
            https://id.twitch.tv/oauth2/token?client_id=$clientId&client_secret=$clientSecret&grant_type=client_credentials""".trimIndent())
        val conn = url.openConnection() as HttpsURLConnection
        conn.requestMethod = "POST"

        val body = String((conn.content as InputStream).buffered().readAllBytes())
        val jObj = JSONObject(body)
        val token = jObj.getString("access_token")
        val expires = LocalDateTime.now().plusSeconds(jObj.getLong("expires_in"))

        return Pair(token, expires)
    }
}

enum class RequestMethod {
    GET, POST
}