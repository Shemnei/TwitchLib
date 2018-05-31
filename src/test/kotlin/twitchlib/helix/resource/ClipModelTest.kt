package twitchlib.helix.resource

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import twitchlib.util.utcToDefault
import java.time.LocalDateTime
import java.util.*

internal class ClipModelTest {

    @Test
    fun parseClipTest() {
        val dataString = """
            {
                "id": "AwkwardHelplessSalamanderSwiftRage",
                "url": "https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
                "embed_url": "https://clips.twitch.tv/embed?clip=AwkwardHelplessSalamanderSwiftRage",
                "broadcaster_id": "67955580",
                "creator_id": "53834192",
                "video_id": "205586603",
                "game_id": "488191",
                "language": "en",
                "title": "babymetal",
                "view_count": 10,
                "created_at": "2017-11-30T22:34:18Z",
                "thumbnail_url": "https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg"
            }
        """.trimIndent()
        val dt = LocalDateTime.of(2017, 11, 30, 22, 34, 18).utcToDefault()

        val jObj = JSONObject(dataString)
        val clip = Clip(jObj)

        assertNotNull(clip)
        assertEquals("AwkwardHelplessSalamanderSwiftRage", clip.id)
        assertEquals("https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage", clip.url)
        assertEquals("https://clips.twitch.tv/embed?clip=AwkwardHelplessSalamanderSwiftRage", clip.embedUrl)
        assertEquals(67955580L, clip.broadcasterId)
        assertEquals(53834192L, clip.creatorId)
        assertEquals("205586603", clip.videoId)
        assertEquals(488191, clip.gameId)
        assertEquals(Locale.ENGLISH, clip.language)
        assertEquals("babymetal", clip.title)
        assertEquals(10L, clip.viewCount)
        assertEquals(dt, clip.createdAt)
        assertEquals("https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg", clip.thumbnailUrl)
    }

    @Test
    fun parseClipResponseTest() {
        val dataString = """
            {
                "data": [
                    {
                        "id": "RandomClip1",
                        "url": "https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
                        "embed_url": "https://clips.twitch.tv/embed?clip=RandomClip1",
                        "broadcaster_id": "1234",
                        "creator_id": "123456",
                        "video_id": "1234567",
                        "game_id": "33103",
                        "language": "en",
                        "title": "random1",
                        "view_count": 10,
                        "created_at": "2017-11-30T22:34:18Z",
                        "thumbnail_url": "https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg"
                    },
                    {
                        "id": "AwkwardHelplessSalamanderSwiftRage",
                        "url": "https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
                        "embed_url": "https://clips.twitch.tv/embed?clip=AwkwardHelplessSalamanderSwiftRage",
                        "broadcaster_id": "67955580",
                        "creator_id": "53834192",
                        "video_id": "205586603",
                        "game_id": "488191",
                        "language": "en",
                        "title": "babymetal",
                        "view_count": 10,
                        "created_at": "2017-11-30T22:34:18Z",
                        "thumbnail_url": "https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg"
                    }
                ],
                "pagination": {
                    "cursor": "eyJiIjpudWxsLCJhIjoiIn0"
                }
            }
        """.trimIndent()
        val dt = LocalDateTime.of(2017, 11, 30, 22, 34, 18).utcToDefault()

        val jObj = JSONObject(dataString)
        val response = ClipResponse(jObj)

        // response
        assertNotNull(response)
        assertNotNull(response.clips)
        assertEquals(2, response.clips.size)
        assertNotNull(response.pagination)
        assertEquals("eyJiIjpudWxsLCJhIjoiIn0", response.pagination.cursor)

        // clip 1
        val clipOne = response.clips.getOrNull(0)
        assertNotNull(clipOne)
        clipOne!!
        assertEquals("RandomClip1", clipOne.id)
        assertEquals("https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage", clipOne.url)
        assertEquals("https://clips.twitch.tv/embed?clip=RandomClip1", clipOne.embedUrl)
        assertEquals(1234L, clipOne.broadcasterId)
        assertEquals(123456L, clipOne.creatorId)
        assertEquals("1234567", clipOne.videoId)
        assertEquals(33103, clipOne.gameId)
        assertEquals(Locale.ENGLISH, clipOne.language)
        assertEquals("random1", clipOne.title)
        assertEquals(10L, clipOne.viewCount)
        assertEquals(dt, clipOne.createdAt)
        assertEquals("https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg", clipOne.thumbnailUrl)

        // clip 2
        val clipTwo = response.clips.getOrNull(1)
        assertNotNull(clipTwo)
        clipTwo!!
        assertEquals("AwkwardHelplessSalamanderSwiftRage", clipTwo.id)
        assertEquals("https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage", clipTwo.url)
        assertEquals("https://clips.twitch.tv/embed?clip=AwkwardHelplessSalamanderSwiftRage", clipTwo.embedUrl)
        assertEquals(67955580L, clipTwo.broadcasterId)
        assertEquals(53834192L, clipTwo.creatorId)
        assertEquals("205586603", clipTwo.videoId)
        assertEquals(488191, clipTwo.gameId)
        assertEquals(Locale.ENGLISH, clipTwo.language)
        assertEquals("babymetal", clipTwo.title)
        assertEquals(10L, clipTwo.viewCount)
        assertEquals(dt, clipTwo.createdAt)
        assertEquals("https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg", clipTwo.thumbnailUrl)
    }
}