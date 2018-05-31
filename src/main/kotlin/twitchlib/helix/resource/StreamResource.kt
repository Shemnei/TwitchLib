package twitchlib.helix.resource

import org.json.JSONObject
import twitchlib.helix.HelixClient
import twitchlib.util.JsonModel
import twitchlib.util.json
import twitchlib.util.toSystemDateTime
import java.net.URL
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

class StreamResource(
        private val client: HelixClient
) {
    fun getStreams(req: StreamRequest): StreamResponse {
        val url = URL("""https://api.twitch.tv/helix/streams?${req.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        return StreamResponse(jObj)
    }

    fun getStreams(cursor: ResourceCursor): StreamResponse {
        val url = URL("""https://api.twitch.tv/helix/streams?${cursor.query}""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        return StreamResponse(jObj)
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
            throw IllegalArgumentException("Amount can be in [1-100], was $amount")
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

class Stream(override val root: JSONObject) : JsonModel {
    val id: Long by json { it.toString().toLong() }
    val userId: Long by json("user_id") { it.toString().toLong() }
    val gameId: Long by json("game_id") { it.toString().toLong() }
    val communityIds: List<String> by json("community_ids")
    val state: StreamState by json("type") { if (it.toString() == "live") StreamState.ONLINE else StreamState.ERROR }
    val title: String by json()
    val viewerCount: Long by json("viewer_count")
    val language: Locale by json { Locale(it.toString()) }
    val startedAt: LocalDateTime by json("started_at") {
        ZonedDateTime.parse(it.toString()).toSystemDateTime()
    }
    val thumbnailUrl: String by json("thumbnail_url")
}

class StreamResponse(override val root: JSONObject) : JsonModel {
    val streams: List<Stream> by json("data")
    val pagination: Pagination by json()
}