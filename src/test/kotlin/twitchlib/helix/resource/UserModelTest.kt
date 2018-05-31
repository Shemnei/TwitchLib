package twitchlib.helix.resource

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class UserModelTest {

    @Test
    fun parseUserTest() {
        val dataString = """
            {
                "id": "44322889",
                "login": "dallas",
                "display_name": "dallas",
                "type": "staff",
                "broadcaster_type": "",
                "description": "Just a gamer playing games and chatting. :)",
                "profile_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/dallas-profile_image-1a2c906ee2c35f12-300x300.png",
                "offline_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/dallas-channel_offline_image-1a2c906ee2c35f12-1920x1080.png",
                "view_count": 191836881
            }
        """.trimIndent()

        val jObj = JSONObject(dataString)
        val user = User(jObj)

        assertNotNull(user)
        assertEquals(44322889L, user.id)
        assertEquals("dallas", user.login)
        assertEquals("dallas", user.displayName)
        assertEquals("staff", user.type)
        assertEquals("", user.broadcasterType)
        assertEquals("Just a gamer playing games and chatting. :)", user.description)
        assertEquals("https://static-cdn.jtvnw.net/jtv_user_pictures/dallas-profile_image-1a2c906ee2c35f12-300x300.png", user.profileImageUrl)
        assertEquals("https://static-cdn.jtvnw.net/jtv_user_pictures/dallas-channel_offline_image-1a2c906ee2c35f12-1920x1080.png", user.offlineImageUrl)
        assertEquals(191836881L, user.viewCount)
    }

    @Test
    fun parseFollowResponseTest() {
        val dataString = """
            {
                "data": [{
                    "id": "23161357",
                    "login": "lirik",
                    "display_name": "LIRIK",
                    "type": "",
                    "broadcaster_type": "partner",
                    "description": "Your source for a good time, hilarity, and gaming!",
                    "profile_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/40b6713c-3ce7-438d-8956-8b974f6570d2-profile_image-300x300.jpg",
                    "offline_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/4016f45989610006-channel_offline_image-1920x1080.jpeg",
                    "view_count": 244821942
                }, {
                    "id": "7236692",
                    "login": "dansgaming",
                    "display_name": "DansGaming",
                    "type": "",
                    "broadcaster_type": "partner",
                    "description": "Dan is a regular guy with a passion for games! Playing all games new to old. Live everyday at 9am PST // 5pm GMT",
                    "profile_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/32b68813-ea70-43bf-a8e4-a55f3096f408-profile_image-300x300.png",
                    "offline_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/20a1cfc0-412d-4b6b-a28b-f1fd7dcac2fd-channel_offline_image-1920x1080.png",
                    "view_count": 91286918
                }]
            }
        """.trimIndent()

        val jObj = JSONObject(dataString)
        val response = UserResponse(jObj)

        // response
        assertNotNull(response)
        assertNotNull(response.users)
        assertEquals(2, response.users.size)

        // user 1
        val userOne = response.users.getOrNull(0)
        assertNotNull(userOne)
        userOne!!
        assertEquals(23161357L, userOne.id)
        assertEquals("lirik", userOne.login)
        assertEquals("LIRIK", userOne.displayName)
        assertEquals("", userOne.type)
        assertEquals("partner", userOne.broadcasterType)
        assertEquals("Your source for a good time, hilarity, and gaming!", userOne.description)
        assertEquals("https://static-cdn.jtvnw.net/jtv_user_pictures/40b6713c-3ce7-438d-8956-8b974f6570d2-profile_image-300x300.jpg", userOne.profileImageUrl)
        assertEquals("https://static-cdn.jtvnw.net/jtv_user_pictures/4016f45989610006-channel_offline_image-1920x1080.jpeg", userOne.offlineImageUrl)
        assertEquals(244821942L, userOne.viewCount)

        // user 2
        val userTwo = response.users.getOrNull(1)
        assertNotNull(userTwo)
        userTwo!!
        assertEquals(7236692L, userTwo.id)
        assertEquals("dansgaming", userTwo.login)
        assertEquals("DansGaming", userTwo.displayName)
        assertEquals("", userTwo.type)
        assertEquals("partner", userTwo.broadcasterType)
        assertEquals("Dan is a regular guy with a passion for games! Playing all games new to old. Live everyday at 9am PST // 5pm GMT", userTwo.description)
        assertEquals("https://static-cdn.jtvnw.net/jtv_user_pictures/32b68813-ea70-43bf-a8e4-a55f3096f408-profile_image-300x300.png", userTwo.profileImageUrl)
        assertEquals("https://static-cdn.jtvnw.net/jtv_user_pictures/20a1cfc0-412d-4b6b-a28b-f1fd7dcac2fd-channel_offline_image-1920x1080.png", userTwo.offlineImageUrl)
        assertEquals(91286918L, userTwo.viewCount)
    }
}