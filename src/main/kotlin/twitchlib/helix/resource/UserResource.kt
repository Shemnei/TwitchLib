package twitchlib.helix.resource

import org.json.JSONObject
import twitchlib.helix.HelixClient
import twitchlib.util.JsonModel
import twitchlib.util.json
import java.net.URL

class UserResource(
        private val client: HelixClient
) {
    fun getUsers(req: UserRequest): UserResponse {
        val url = URL("""https://api.twitch.tv/helix/users?${req.query}""")
        val body = client.requestAuthorized(url)

        val data = JSONObject(body)
        return UserResponse(data)
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

class User(override val root: JSONObject) : JsonModel {
    val id: Long by json { it.toString().toLong() }
    val login: String by json()
    val displayName: String by json("display_name")
    val type: String by json()
    val broadcasterType: String by json("broadcaster_type")
    val description: String by json()
    val profileImageUrl: String by json("profile_image_url")
    val offlineImageUrl: String by json("offline_image_url")
    val viewCount: Long by json("view_count")
}

class UserResponse(override val root: JSONObject) : JsonModel {
    val users: List<User> by json("data")
}