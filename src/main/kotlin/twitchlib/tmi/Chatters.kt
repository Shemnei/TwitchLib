package twitchlib.tmi

import org.json.JSONObject
import twitchlib.util.JsonModel
import twitchlib.util.json
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// may be cached for several minutes
// undocumented endpoint
object Chatters {

    private const val BASE_URL = "https://tmi.twitch.tv/group/user/%s/chatters"

    fun of(channel: String): ChattersResponse {
        val url = URL(BASE_URL.format(channel))
        val conn = url.openConnection() as HttpsURLConnection

        val body = String((conn.content as InputStream).buffered().readAllBytes())

        val jObj = JSONObject(body)
        return ChattersResponse(jObj)
    }

}

class ChattersResponse(override val root: JSONObject) : JsonModel {
    val chatterCount: Long by json("chatter_count")
//    val chatters: Chatter by json()

    val moderators: List<String> by json("chatters\\moderators")
    val staff: List<String> by json("chatters\\staff")
    val admins: List<String> by json("chatters\\admins")
    val globalMods: List<String> by json("chatters\\global_mods")
    val viewers: List<String> by json("chatters\\viewers")
}