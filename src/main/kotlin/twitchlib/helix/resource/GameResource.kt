package twitchlib.helix.resource

import org.json.JSONArray
import org.json.JSONObject
import twitchlib.helix.HelixClient
import java.net.URL

class GameResource(
        private val client: HelixClient
) {
    fun getGames(req: GameRequest): List<Game> {
        val url = URL("""https://api.twitch.tv/helix/games?${req.query}""")
        val body = client.requestAuthorized(url)

        val data = JSONObject(body).getJSONArray("data")
        return parseGames(data)
    }

    fun getTopGames(amount: Int = 20, cursor: ResourceCursor? = null): GameResponse {
        if (amount > 100 || amount < 1)
            throw IllegalArgumentException("Amount must be in [1-100], was $amount")

        val optional = cursor?.query ?: ""

        val url = URL("""https://api.twitch.tv/helix/games/top?first=$amount&$optional""")
        val body = client.requestAuthorized(url)

        val jObj = JSONObject(body)
        val nextCursor = jObj.getJSONObject("pagination").getString("cursor")
        val data = jObj.getJSONArray("data")
        val games = parseGames(data)

        return GameResponse(games, nextCursor)
    }

    private fun parseGames(data: JSONArray): List<Game> {
        return data.map {
            val d = it as JSONObject
            val id = d.getString("id").toLong()
            val name = d.getString("name")
            val boxArtUrl = d.getString("box_art_url")
            Game(id, name, boxArtUrl)
        }
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
            idStrings.add("id=" + ids.joinToString("%20"))
        if (names.isNotEmpty())
            idStrings.add("name=" + names.joinToString("%20"))
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

data class Game(
        val id: Long,
        val name: String,
        val boxArtUrl: String
)

data class GameResponse(
        val games: List<Game>,
        val cursor: String
)