package twitchlib.tmi.message

/**
 * Created on 05.03.18.
 */

enum class MessageType {
    PING,
    PONG,

    CAP,

    _001,
    _002,
    _003,
    _004,
    _353,
    _366,
    _372,
    _375,
    _376,

    _421,

    JOIN,
    PART,
    PRIVMSG,
    WHISPER,
    NAMES,

    CLEARCHAT,
    HOSTTARGET,
    NOTICE,
    RECONNECT,
    ROOMSTATE,
    USERNOTICE,
    USERSTATE,

    MODE,
    GLOBALUSERSTATE;

    companion object {
        val numberRegex = Regex("""[0-9]+""")
        fun getCommand(cmd: String): MessageType {
            val pre = if (cmd.matches(numberRegex)) "_" else ""
            return MessageType.valueOf(pre + cmd)
        }
    }
}