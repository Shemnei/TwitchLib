package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import twitchlib.util.JsonModel
import twitchlib.util.json
import twitchlib.util.toSystemDateTime
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class ClipResource(
        private val client: HelixClient
) {
    fun getClips(req: ClipRequest): ClipResponse {
        val url = URL("""https://api.twitch.tv/helix/clips?${req.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        return ClipResponse(jObj)
    }

    fun getClips(cursor: ResourceCursor): ClipResponse {
        val url = URL("""https://api.twitch.tv/helix/clips?${cursor.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        return ClipResponse(jObj)
    }
}

enum class ClipIdType {
    BROADCASTER_ID, GAME_ID, ID
}

class ClipRequest private constructor(
        val id: String,
        val type: ClipIdType,
        val amount: Int
) {
    init {
        if (amount > 100 || amount < 1)
            throw IllegalArgumentException("Amount can be in [1-100], was $amount")
    }

    val query: String by lazy {
        "${type.name.toLowerCase()}=$id&first=$amount"
    }

    companion object {
        fun ofBroadcaster(broadcasterId: Long, amount: Int = 20): ClipRequest {
            return ClipRequest(broadcasterId.toString(), ClipIdType.BROADCASTER_ID, amount)
        }

        fun ofGame(gameId: Long, amount: Int = 20): ClipRequest {
            return ClipRequest(gameId.toString(), ClipIdType.GAME_ID, amount)
        }

        fun ofClipIds(vararg ids: String, amount: Int = 20): ClipRequest {
            return ClipRequest(ids.joinToString("%20"), ClipIdType.ID, amount)
        }
    }
}

class Clip(override val root: JSONObject) : JsonModel {
    val id: String by json()
    val url: String by json()
    val embedUrl: String  by json("embed_url")
    val broadcasterId: Long by json("broadcaster_id") { it.toString().toLong() }
    val creatorId: Long by json("creator_id") { it.toString().toLong() }
    val videoId: String by json("video_id")
    val gameId: Long by json("game_id") { it.toString().toLong() }
    val language: Locale by json { Locale(it.toString()) }
    val title: String by json()
    val viewCount: Long by json("view_count")
    val createdAt: LocalDateTime by json("created_at") {
        ZonedDateTime.parse(it.toString()).toSystemDateTime()
    }
    val thumbnailUrl: String by json("thumbnail_url")
}

class ClipResponse(override val root: JSONObject) : JsonModel {
    val clips: List<Clip> by json("data")
    val pagination: Pagination by json()
}