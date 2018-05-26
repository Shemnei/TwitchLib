package twitchlib.helix.resource

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