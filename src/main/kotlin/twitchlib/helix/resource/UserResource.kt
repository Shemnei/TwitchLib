package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import java.net.URL

class UserResource(
        private val client: HelixClient
) {
    fun getUsers(req: UserRequest): List<User> {
        val url = URL("""https://api.twitch.tv/helix/users?${req.query}""")
        val body = client.requestAuthorized(url)

        val data = JSONObject(body).getJSONArray("data")
        return parseUsers(data)
    }

    private fun parseUsers(data: JSONArray): List<User> {
        return data.map {
            val d = it as JSONObject
            val id = d.getString("id").toLong()
            val login = d.getString("login")
            val displayName = d.getString("display_name")
            val type = d.getString("type")
            val broadcasterType = d.getString("broadcaster_type")
            val description = d.getString("description")
            val profileImageUrl = d.getString("profile_image_url")
            val offlineImageUrl = d.getString("offline_image_url")
            val view_count = d.getLong("view_count")
            User(id, login, displayName, type, broadcasterType, description, profileImageUrl, offlineImageUrl, view_count)
        }
    }

}

class UserRequest private constructor(
        val ids: Array<out String> = emptyArray(),
        val logins: Array<out String> = emptyArray()
) {

    init {
        val idAmount = ids.size + logins.size
        if (idAmount > 100 || idAmount == 0)
            throw IllegalArgumentException("Amount of ids(id/logins) must be in [1-100], was $idAmount")
    }

    val query: String by lazy {
        val idStrings = mutableListOf<String>()
        if (ids.isNotEmpty())
            idStrings.add(ids.joinToString("&id=", prefix = "id="))
        if (logins.isNotEmpty())
            idStrings.add(logins.joinToString("&login=", prefix = "login="))
        idStrings.joinToString("&")
    }

    companion object {
        fun ofIds(vararg ids: Long): UserRequest {
            return UserRequest(ids.map { it.toString() }.toTypedArray())
        }

        fun ofLogins(vararg logins: String): UserRequest {
            return UserRequest(logins = logins)
        }

        fun ofMixed(ids: Array<Long>, names: Array<String>): UserRequest {
            return UserRequest(ids.map { it.toString() }.toTypedArray(), names)
        }
    }
}

data class User(
        val id: Long,
        val login: String,
        val displayName: String,
        val type: String,
        val broadcasterType: String,
        val description: String,
        val profileImageUrl: String,
        val offlineImageUrl: String,
        val view_count: Long
)