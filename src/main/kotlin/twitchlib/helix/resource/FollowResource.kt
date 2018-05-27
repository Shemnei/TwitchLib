package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime


class FollowResource(
        private val client: HelixClient
) {
    fun getFollows(req: FollowRequest): FollowResponse {
        val url = URL("""https://api.twitch.tv/helix/users/follows?${req.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        val data = jObj.getJSONArray("data")
        val total = jObj.getLong("total")
        val cursor = jObj.getJSONObject("pagination").getString("cursor")
        val follows = parseFollows(data)

        return FollowResponse(total, follows, cursor)
    }

    private fun parseFollows(data: JSONArray): List<Follow> {
        return data.map {
            val d = it as JSONObject
            val fromUserId = d.getString("from_id")
            val toUserId = d.getString("to_id")
            val followedAt = d.getString("followed_at")
            Follow(fromUserId, toUserId, followedAt)
        }
    }

}

class FollowRequest private constructor(
        val fromId: Long = -1,
        val toId: Long = -1,
        val amount: Int = 20
) {

    init {
        if (amount > 100 || amount < 1)
            throw IllegalArgumentException("Amount must be in [1-100], was $amount")
    }

    val query: String by lazy {
        val idStrings = mutableListOf<String>()
        if (fromId > 0)
            idStrings.add("from_id=$fromId")
        if (toId > 0)
            idStrings.add("to_id=$toId")
        idStrings.add("first=$amount")
        idStrings.joinToString("&")
    }

    companion object {
        fun from(fromUserId: Long, amount: Int = 20): FollowRequest {
            return FollowRequest(fromId = fromUserId, amount = amount)
        }

        fun to(toUserId: Long, amount: Int = 20): FollowRequest {
            return FollowRequest(toId = toUserId, amount = amount)
        }

        fun relationship(fromUserId: Long, toUserId: Long): FollowRequest {
            return FollowRequest(fromUserId, toUserId, 1)
        }
    }
}

class Follow(
        _fromUserId: String,
        _toUserId: String,
        _followedAt: String
) {
    val fromUserId: Long = _fromUserId.toLong()
    val toUserId: Long = _toUserId.toLong()
    val followedAt: LocalDateTime by lazy {
        LocalDateTime.ofInstant(ZonedDateTime.parse(_followedAt).toInstant(), ZoneId.systemDefault())
    }
}

data class FollowResponse(
        val total: Long,
        val follows: List<Follow>,
        val cursor: String
)