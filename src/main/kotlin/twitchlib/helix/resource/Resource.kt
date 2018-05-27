package twitchlib.helix.resource

import org.json.JSONObject
import twitchlib.util.JsonModel
import twitchlib.util.json

enum class CursorType {
    AFTER, BEFORE
}

data class ResourceCursor(
        val id: String,
        val type: CursorType
) {
    val query: String by lazy {
        "${type.name.toLowerCase()}=id"
    }
}

class Pagination(override val root: JSONObject) : JsonModel {
    val cursor: String by json()
}