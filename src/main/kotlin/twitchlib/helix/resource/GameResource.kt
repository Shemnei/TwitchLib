package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import twitchlib.util.JsonModel
import twitchlib.util.json
import java.net.URL
import java.net.URLEncoder

class GameResource(
        private val client: HelixClient
) {
    fun getGames(req: GameRequest): SimpleGameResponse {
        val url = URL("""https://api.twitch.tv/helix/games?${req.query}""")
        val body = client.requestAuthorized(url)

        val data = JSONObject(body)
        return SimpleGameResponse(data)
    }

    fun getTopGames(amount: Int = 20, cursor: ResourceCursor? = null): GameResponse {
        if (amount > 100 || amount < 1)
            throw IllegalArgumentException("Amount must be in [1-100], was $amount")

        val optional = cursor?.query ?: ""

        val url = URL("""https://api.twitch.tv/helix/games/top?first=$amount&$optional""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        return GameResponse(jObj)
    }
}

class GameRequest private constructor(
        val ids: Array<out String> = emptyArray(),
        val names: Array<out String> = emptyArray()
) {

    init {
        val idAmount = ids.size + names.size
        if (idAmount > 100 || idAmount == 0)
            throw IllegalArgumentException("Amount of ids(id/name) must be in [1-100], was $idAmount")
    }

    val query: String by lazy {
        val idStrings = mutableListOf<String>()
        if (ids.isNotEmpty())
            idStrings.add(ids.joinToString("&id=", prefix = "id="))
        if (names.isNotEmpty())
            idStrings.add(names.joinToString("&name=", prefix = "name=") {
                URLEncoder.encode(it, "utf-8")
            })
        idStrings.joinToString("&")
    }

    companion object {
        fun ofIds(vararg ids: Long): GameRequest {
            return GameRequest(ids.map { it.toString() }.toTypedArray())
        }

        fun ofNames(vararg names: String): GameRequest {
            return GameRequest(names = names)
        }

        fun ofMixed(ids: Array<Long>, names: Array<String>): GameRequest {
            return GameRequest(ids.map { it.toString() }.toTypedArray(), names)
        }
    }
}

class Game(override val root: JSONObject) : JsonModel {
    val id: Long by json { it.toString().toLong() }
    val name: String by json()
    val boxArtUrl: String by json("box_art_url")
}

open class SimpleGameResponse(override val root: JSONObject) : JsonModel {
    val games: List<Game> by json("data")
}

class GameResponse(override val root: JSONObject) : JsonModel, SimpleGameResponse(root) {
    val pagination: Pagination by json()
}