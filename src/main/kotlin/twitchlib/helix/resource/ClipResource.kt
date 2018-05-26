package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import java.net.URL

class ClipResource(
        private val client: HelixClient
) {
    fun getClips(req: ClipRequest): ClipResponse {
        val url = URL("""https://api.twitch.tv/helix/clips?${req.query}""")
        val body = client.requestAuthorized(url)


        val jObj = JSONObject(body)
        val data = jObj.getJSONArray("data")
        val clips = parseClips(data)
        val cursor = jObj.getJSONObject("pagination").getString("cursor")

        return ClipResponse(clips, cursor)
    }

    fun getClips(cursor: ResourceCursor): ClipResponse {
        val url = URL("""https://api.twitch.tv/helix/clips?${cursor.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        val data = jObj.getJSONArray("data")
        val clips = parseClips(data)
        val newCursor = jObj.getJSONObject("pagination").getString("cursor")

        return ClipResponse(clips, newCursor)
    }

    private fun parseClips(data: JSONArray): List<Clip> {
        return data.map {
            val d = it as JSONObject
            val id = d.getString("id")
            val url = d.getString("url")
            val embedUrl = d.getString("embed_url")
            val broadcasterId = d.getString("broadcaster_id").toLong()
            val creatorId = d.getString("creator_id").toLong()
            val videoId = d.getString("video_id")
            val gameId = d.getString("game_id").toLong()
            val language = d.getString("language")
            val title = d.getString("title")
            val viewCount = d.getLong("view_count")
            val createdAt = d.getString("created_at")
            val thumbnailUrl = d.getString("thumbnail_url")
            Clip(id, url, embedUrl, broadcasterId, creatorId, if (videoId.isBlank()) null else videoId.toLong(), gameId, language, title, viewCount, createdAt, thumbnailUrl)
        }
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
        "${type.name.toLowerCase()}=$id?first=$amount"
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

data class Clip(
        val id: String,
        val url: String,
        val embedUrl: String,
        val broadcasterId: Long,
        val creatorId: Long,
        val videoId: Long?,
        val gameId: Long,
        val language: String,
        val title: String,
        val viewCount: Long,
        val createdAt: String,
        val thumbnailUrl: String
)

data class ClipResponse(
        val clips: List<Clip>,
        val cursor: String
)