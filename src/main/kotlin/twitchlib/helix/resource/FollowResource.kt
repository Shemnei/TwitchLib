package twitchlib.helix.resource

import org.json.JSONObject
import twitchlib.helix.HelixClient
import twitchlib.util.JsonModel
import twitchlib.util.json
import twitchlib.util.toSystemDateTime
import java.net.URL
import java.time.LocalDateTime
import java.time.ZonedDateTime


class FollowResource(
        private val client: HelixClient
) {
    fun getFollows(req: FollowRequest): FollowResponse {
        val url = URL("""https://api.twitch.tv/helix/users/follows?${req.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        return FollowResponse(jObj)
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

class Follow(override val root: JSONObject) : JsonModel {
    val fromUserId: Long by json("from_id") { it.toString().toLong() }
    val toUserId: Long by json("to_id") { it.toString().toLong() }
    val followedAt: LocalDateTime by json("followed_at") {
        ZonedDateTime.parse(it.toString()).toSystemDateTime()
    }
}

data class FollowResponse(override val root: JSONObject) : JsonModel {
    val total: Long by json()
    val follows: List<Follow> by json("data")
    val pagination: Pagination by json()
}