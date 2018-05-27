package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class StreamResource(
        private val client: HelixClient
) {
    fun getStreams(req: StreamRequest): StreamResponse {
        val url = URL("""https://api.twitch.tv/helix/streams?${req.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        val data = jObj.getJSONArray("data")
        val cursor = jObj.getJSONObject("pagination").getString("cursor")
        val streams = parseStreams(data)

        return StreamResponse(streams, cursor)
    }

    fun getStreams(cursor: ResourceCursor): StreamResponse {
        val url = URL("""https://api.twitch.tv/helix/streams?${cursor.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        val data = jObj.getJSONArray("data")
        val newCursor = jObj.getJSONObject("pagination").getString("cursor")
        val streams = parseStreams(data)

        return StreamResponse(streams, newCursor)
    }

    private fun parseStreams(data: JSONArray): List<Stream> {
        return data.map {
            val d = it as JSONObject
            val id = d.getString("id")
            val userId = d.getString("user_id")
            val gameId = d.getString("game_id")
            val communityIds = d.getJSONArray("community_ids").map { it.toString() }
            val type = d.getString("type")
            val title = d.getString("title")
            val viewCount = d.getLong("viewer_count")
            val startedAt = d.getString("started_at")
            val language = d.getString("language")
            val thumbnailUrl = d.getString("thumbnail_url")
            Stream(id, userId, gameId, communityIds, type, title, viewCount, startedAt, language, thumbnailUrl)
        }
    }
}

enum class StreamIdType {
    COMMUNITY_ID, GAME_ID, LANGUAGE, USER_ID, USER_LOGIN
}

class StreamRequest private constructor(
        val communityIds: Array<out String> = emptyArray(),
        val gameIds: Array<out String> = emptyArray(),
        val languages: Array<out String> = emptyArray(),
        val userIds: Array<out String> = emptyArray(),
        val userLogins: Array<out String> = emptyArray(),
        val amount: Int = 20
) {

    init {
        if (amount > 100 || amount < 1)
            throw IllegalArgumentException("Amount can be in [0-100], was $amount")
        val idAmount = communityIds.size + gameIds.size + languages.size + userIds.size + userLogins.size
        if (idAmount > 100)
            throw IllegalArgumentException("Amount of overall ids must be in [0-100], was $idAmount")
    }

    val query: String by lazy {
        val idStrings = mutableListOf<String>()
        if (communityIds.isNotEmpty())
            idStrings.add(communityIds.joinToString("&community_id=", prefix = "community_id="))
        if (gameIds.isNotEmpty())
            idStrings.add(gameIds.joinToString("&game_id=", prefix = "game_id="))
        if (languages.isNotEmpty())
            idStrings.add(languages.joinToString("&language=", prefix = "language="))
        if (userIds.isNotEmpty())
            idStrings.add(userIds.joinToString("&user_id=", prefix = "user_id="))
        if (userLogins.isNotEmpty())
            idStrings.add(userLogins.joinToString("&user_login=", prefix = "user_login="))
        idStrings.add("first=$amount")
        idStrings.joinToString("&")
    }

    companion object {
        fun ofCommunityIds(vararg communityIds: String, amount: Int = 20): StreamRequest {
            return StreamRequest(communityIds = communityIds, amount = amount)
        }

        fun ofGameIds(vararg gameIds: Long, amount: Int = 20): StreamRequest {
            return StreamRequest(gameIds = gameIds.map { it.toString() }.toTypedArray(), amount = amount)
        }

        // TODO: 26.05.2018 maybe string -> locale
        fun ofLanguages(vararg languages: String, amount: Int = 20): StreamRequest {
            return StreamRequest(languages = languages, amount = amount)
        }

        fun ofUserIds(vararg userIds: Long, amount: Int = 20): StreamRequest {
            return StreamRequest(userIds = userIds.map { it.toString() }.toTypedArray(), amount = amount)
        }

        fun ofUserLogins(vararg userLogins: String, amount: Int = 20): StreamRequest {
            return StreamRequest(userLogins = userLogins, amount = amount)
        }

        fun ofMixed(
                communityIds: Array<String> = emptyArray(),
                gameIds: Array<Long> = emptyArray(),
                languages: Array<String> = emptyArray(),
                userIds: Array<Long> = emptyArray(),
                userLogins: Array<String> = emptyArray(),
                amount: Int = 20
        ): StreamRequest {
            return StreamRequest(
                    communityIds,
                    gameIds.map { it.toString() }.toTypedArray(),
                    languages,
                    userIds.map { it.toString() }.toTypedArray(),
                    userLogins,
                    amount
            )
        }

        fun ofTop(amount: Int = 20): StreamRequest {
            return StreamRequest(amount = amount)
        }
    }
}

enum class StreamState {
    ONLINE, OFFLINE, ERROR
}

class Stream(
        _id: String,
        _userId: String,
        _gameId: String,
        val communityIds: List<String>,
        _type: String,
        val title: String,
        val viewCount: Long,
        _startedAt: String,
        _language: String,
        val thumbnailUrl: String
) {
    val id: Long = _id.toLong()
    val userId: Long = _userId.toLong()
    val gameId: Long = _gameId.toLong()
    val state: StreamState = if (_type == "live") StreamState.ONLINE else StreamState.ERROR
    val language: Locale by lazy { Locale(_language) }
    val startedAt: LocalDateTime by lazy {
        LocalDateTime.ofInstant(ZonedDateTime.parse(_startedAt).toInstant(), ZoneId.systemDefault())
    }
}

data class StreamResponse(
        val streams: List<Stream>,
        val cursor: String
)