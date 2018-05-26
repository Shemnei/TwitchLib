package twitchlib.tmi.message

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

data class Message private constructor(
        val tagCompound: TagCompound?,
        val sender: String,
        val command: Command,
        val receiver: String?,
        val message: String?,
        val raw: String? = null
) {

    companion object {
        val msgPattern = Pattern.compile("""^(?:@(?<tags>[^\s]+))?(?:\s?:(?<sender>[^\s!]+)(?:[^ ]+)?)(?:\s(?<command>[^\s]+))(?:\s#?(?<receiver>[^\s]+)(?:\s:?(?<message>.*))?)?""")

        fun parse(msg: String): Message {
            if (msg.startsWith("PING :")) {
                return Message(null, "", Command.PING, null, msg.split(" ")[1].drop(1))
            }

            val matcher = msgPattern.matcher(msg)
            val found = matcher.find()
            if (!found) {
                // TODO: 06.03.18 Add ParseError
                throw NullPointerException("Couldn't parse as Message: $msg")
            }
            val tagString = matcher.group("tags")
            val sender = matcher.group("sender")
            val command = try {
                Command.getCommand(matcher.group("command"))
            } catch (e: Exception) {
                System.err.println("${DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())} - ${e.message}")
                Command.PRIVMSG
            }
            val receiver = matcher.group("receiver")
            val message = matcher.group("message")
            return Message(tagString?.let { TagCompound.parse(it) }, sender, command, receiver, message, msg)
        }
    }
}