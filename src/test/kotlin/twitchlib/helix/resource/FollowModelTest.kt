package twitchlib.helix.resource

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import twitchlib.util.utcToDefault
import java.time.LocalDateTime

internal class FollowModelTest {

    @Test
    fun parseFollowTest() {
        val dataString = """
            {
                "from_id": "171003792",
                "to_id": "23161357",
                "followed_at": "2017-08-22T22:55:24Z"
            }
        """.trimIndent()
        val dt = LocalDateTime.of(2017, 8, 22, 22, 55, 24).utcToDefault()

        val jObj = JSONObject(dataString)
        val follow = Follow(jObj)

        assertNotNull(follow)
        assertEquals(171003792L, follow.fromUserId)
        assertEquals(23161357L, follow.toUserId)
        assertEquals(dt, follow.followedAt)
    }

    @Test
    fun parseFollowResponseTest() {
        val dataString = """
            {
                "total": 12345,
                "data": [{
                        "from_id": "171003792",
                        "to_id": "23161357",
                        "followed_at": "2017-08-22T22:55:24Z"
                    },
                    {
                        "from_id": "113627897",
                        "to_id": "23161357",
                        "followed_at": "2017-08-22T22:55:04Z"
                    }
                ],
                "pagination": {
                    "cursor": "eyJiIjpudWxsLCJhIjoiMTUwMzQ0MTc3NjQyNDQyMjAwMCJ9"
                }
            }
        """.trimIndent()
        val dtOne = LocalDateTime.of(2017, 8, 22, 22, 55, 24).utcToDefault()
        val dtTwo = LocalDateTime.of(2017, 8, 22, 22, 55, 4).utcToDefault()

        val jObj = JSONObject(dataString)
        val response = FollowResponse(jObj)

        // response
        assertNotNull(response)
        assertEquals(12345L, response.total)
        assertNotNull(response.follows)
        assertEquals(2, response.follows.size)
        assertNotNull(response.pagination)
        assertEquals("eyJiIjpudWxsLCJhIjoiMTUwMzQ0MTc3NjQyNDQyMjAwMCJ9", response.pagination.cursor)

        // follow 1
        val followOne = response.follows.getOrNull(0)
        assertNotNull(followOne)
        followOne!!
        assertEquals(171003792L, followOne.fromUserId)
        assertEquals(23161357L, followOne.toUserId)
        assertEquals(dtOne, followOne.followedAt)

        // follow 2
        val followTwo = response.follows.getOrNull(1)
        assertNotNull(followTwo)
        followTwo!!
        assertEquals(113627897L, followTwo.fromUserId)
        assertEquals(23161357L, followTwo.toUserId)
        assertEquals(dtTwo, followTwo.followedAt)
    }
}