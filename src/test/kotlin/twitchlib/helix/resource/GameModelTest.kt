package twitchlib.helix.resource

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import twitchlib.util.utcToDefault
import java.time.LocalDateTime

internal class GameModelTest {

    @Test
    fun parseGameTest() {
        val dataString = """
            {
                "id": "493057",
                "name": "PLAYERUNKNOWN'S BATTLEGROUNDS",
                "box_art_url": "https://static-cdn.jtvnw.net/ttv-boxart/PLAYERUNKNOWN%27S%20BATTLEGROUNDS-{width}x{height}.jpg"
            }
        """.trimIndent()

        val jObj = JSONObject(dataString)
        val game = Game(jObj)

        assertNotNull(game)
        assertEquals(493057L, game.id)
        assertEquals("PLAYERUNKNOWN'S BATTLEGROUNDS", game.name)
        assertEquals("https://static-cdn.jtvnw.net/ttv-boxart/PLAYERUNKNOWN%27S%20BATTLEGROUNDS-{width}x{height}.jpg", game.boxArtUrl)
    }

    @Test
    fun parseGameResponseTest() {
        val dataString = """
            {
                "data": [{
                    "id": "21779",
                    "name": "League of Legends",
                    "box_art_url": "https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-{width}x{height}.jpg"
                }, {
                    "id": "29595",
                    "name": "Dota 2",
                    "box_art_url": "https://static-cdn.jtvnw.net/ttv-boxart/Dota%202-{width}x{height}.jpg"
                }],
                "pagination": {
                    "cursor": "eyJiIjpudWxsLCJhIjp7Ik9mZnNldCI6Mn19"
                }
            }
        """.trimIndent()

        val jObj = JSONObject(dataString)
        val response = GameResponse(jObj)

        // response
        assertNotNull(response)
        assertNotNull(response.games)
        assertEquals(2, response.games.size)
        assertNotNull(response.pagination)
        assertEquals("eyJiIjpudWxsLCJhIjp7Ik9mZnNldCI6Mn19", response.pagination.cursor)

        // game 1
        val gameOne = response.games.getOrNull(0)
        assertNotNull(gameOne)
        gameOne!!
        assertEquals(21779L, gameOne.id)
        assertEquals("League of Legends", gameOne.name)
        assertEquals("https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-{width}x{height}.jpg", gameOne.boxArtUrl)

        // stream 2
        val gameTwo = response.games.getOrNull(1)
        assertNotNull(gameTwo)
        gameTwo!!
        assertEquals(29595L, gameTwo.id)
        assertEquals("Dota 2", gameTwo.name)
        assertEquals("https://static-cdn.jtvnw.net/ttv-boxart/Dota%202-{width}x{height}.jpg", gameTwo.boxArtUrl)
    }
}