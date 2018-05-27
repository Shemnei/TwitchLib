package twitchlib.helix.resource

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import twitchlib.util.utcToDefault
import java.time.LocalDateTime
import java.util.*

internal class StreamModelTest {

    @Test
    fun parseStreamTest() {
        val dataString = """
            {
                "id": "26007494656",
                "user_id": "23161357",
                "game_id": "417752",
                "community_ids": [
                    "5181e78f-2280-42a6-873d-758e25a7c313",
                    "848d95be-90b3-44a5-b143-6e373754c382",
                    "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"
                ],
                "type": "live",
                "title": "Hey Guys, It's Monday - Twitter: @Lirik",
                "viewer_count": 32575,
                "started_at": "2017-08-14T16:08:32Z",
                "language": "en",
                "thumbnail_url": "https://static-cdn.jtvnw.net/previews-ttv/live_user_lirik-{width}x{height}.jpg"
            }
        """.trimIndent()
        val dt = LocalDateTime.of(2017, 8, 14, 16, 8, 32).utcToDefault()

        val jObj = JSONObject(dataString)
        val stream = Stream(jObj)

        assertNotNull(stream)
        assertEquals(26007494656L, stream.id)
        assertEquals(23161357L, stream.userId)
        assertEquals(417752L, stream.gameId)
        assertEquals(3, stream.communityIds.size)
        assertTrue(stream.communityIds.contains("5181e78f-2280-42a6-873d-758e25a7c313"))
        assertTrue(stream.communityIds.contains("848d95be-90b3-44a5-b143-6e373754c382"))
        assertTrue(stream.communityIds.contains("fd0eab99-832a-4d7e-8cc0-04d73deb2e54"))
        assertEquals(StreamState.ONLINE, stream.state)
        assertEquals("Hey Guys, It's Monday - Twitter: @Lirik", stream.title)
        assertEquals(32575L, stream.viewerCount)
        assertEquals(dt, stream.startedAt)
        assertEquals(Locale.ENGLISH, stream.language)
        assertEquals("https://static-cdn.jtvnw.net/previews-ttv/live_user_lirik-{width}x{height}.jpg", stream.thumbnailUrl)
    }

    @Test
    fun parseStreamResponseTest() {
        val dataString = """
            {
                "data": [
                    {
                        "id": "26007494656",
                        "user_id": "23161357",
                        "game_id": "417752",
                        "community_ids": [
                            "5181e78f-2280-42a6-873d-758e25a7c313",
                            "848d95be-90b3-44a5-b143-6e373754c382",
                            "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"
                        ],
                        "type": "live",
                        "title": "Hey Guys, It's Monday - Twitter: @Lirik",
                        "viewer_count": 32575,
                        "started_at": "2017-08-14T16:08:32Z",
                        "language": "en",
                        "thumbnail_url": "https://static-cdn.jtvnw.net/previews-ttv/live_user_lirik-{width}x{height}.jpg"
                    },
                    {
                        "id": "26007351216",
                        "user_id": "7236692",
                        "game_id": "29307",
                        "community_ids": [
                            "848d95be-90b3-44a5-b143-6e373754c382",
                            "fd0eab99-832a-4d7e-8cc0-04d73deb2e54",
                            "ff1e77af-551d-4993-945c-f8ceaa2a2829"
                        ],
                        "type": "live",
                        "title": "[Punday Monday] Necromancer - Dan's First Character - Maps - !build",
                        "viewer_count": 5723,
                        "started_at": "2017-08-14T15:45:17Z",
                        "language": "en",
                        "thumbnail_url": "https://static-cdn.jtvnw.net/previews-ttv/live_user_dansgaming-{width}x{height}.jpg"
                    }
                ],
                "pagination": {
                    "cursor": "eyJiIjp7Ik9mZnNldCI6MH0sImEiOnsiT2Zmc2V0Ijo0MH19"
                }
            }
        """.trimIndent()
        val dtOne = LocalDateTime.of(2017, 8, 14, 16, 8, 32).utcToDefault()
        val dtTwo = LocalDateTime.of(2017, 8, 14, 15, 45, 17).utcToDefault()

        val jObj = JSONObject(dataString)
        val response = StreamResponse(jObj)

        // response
        assertNotNull(response)
        assertNotNull(response.streams)
        assertEquals(2, response.streams.size)
        assertNotNull(response.pagination)
        assertEquals("eyJiIjp7Ik9mZnNldCI6MH0sImEiOnsiT2Zmc2V0Ijo0MH19", response.pagination.cursor)

        // stream 1
        val streamOne = response.streams.getOrNull(0)
        assertNotNull(streamOne)
        streamOne!!
        assertEquals(26007494656L, streamOne.id)
        assertEquals(23161357L, streamOne.userId)
        assertEquals(417752L, streamOne.gameId)
        assertEquals(3, streamOne.communityIds.size)
        assertTrue(streamOne.communityIds.contains("5181e78f-2280-42a6-873d-758e25a7c313"))
        assertTrue(streamOne.communityIds.contains("848d95be-90b3-44a5-b143-6e373754c382"))
        assertTrue(streamOne.communityIds.contains("fd0eab99-832a-4d7e-8cc0-04d73deb2e54"))
        assertEquals(StreamState.ONLINE, streamOne.state)
        assertEquals("Hey Guys, It's Monday - Twitter: @Lirik", streamOne.title)
        assertEquals(32575L, streamOne.viewerCount)
        assertEquals(dtOne, streamOne.startedAt)
        assertEquals(Locale.ENGLISH, streamOne.language)
        assertEquals("https://static-cdn.jtvnw.net/previews-ttv/live_user_lirik-{width}x{height}.jpg", streamOne.thumbnailUrl)

        // stream 2
        val streamTwo = response.streams.getOrNull(1)
        assertNotNull(streamTwo)
        streamTwo!!
        assertEquals(26007351216L, streamTwo.id)
        assertEquals(7236692L, streamTwo.userId)
        assertEquals(29307L, streamTwo.gameId)
        assertEquals(3, streamTwo.communityIds.size)
        assertTrue(streamTwo.communityIds.contains("848d95be-90b3-44a5-b143-6e373754c382"))
        assertTrue(streamTwo.communityIds.contains("fd0eab99-832a-4d7e-8cc0-04d73deb2e54"))
        assertTrue(streamTwo.communityIds.contains("ff1e77af-551d-4993-945c-f8ceaa2a2829"))
        assertEquals(StreamState.ONLINE, streamTwo.state)
        assertEquals("[Punday Monday] Necromancer - Dan's First Character - Maps - !build", streamTwo.title)
        assertEquals(5723L, streamTwo.viewerCount)
        assertEquals(dtTwo, streamTwo.startedAt)
        assertEquals(Locale.ENGLISH, streamTwo.language)
        assertEquals("https://static-cdn.jtvnw.net/previews-ttv/live_user_dansgaming-{width}x{height}.jpg", streamTwo.thumbnailUrl)
    }
}