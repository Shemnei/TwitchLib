package twitchlib.tmi.message

/**
 * Created on 05.03.18.
 */

enum class Tag {
    BADGES,
    BAN_DURATION,
    BAN_REASON,
    BITS,
    BROADCASTER_LANG,
    COLOR,
    DISPLAY_NAME,
    EMOTES,
    EMOTE_SETS,
    ID,
    LOGIN,
    MOD,
    MSG_ID,
    R9K,
    ROOM_ID,
    SLOW,
    SUBSCRIBER,
    SUBS_ONLY,
    SYSTEM_MSG,
    TMI_SENT_TS,
    TURBO,
    USER_ID,
    USER_TYPE,

    MSG_PARAM_DISPLAYNAME,
    MSG_PARAM_LOGIN,
    MSG_PARAM_MONTHS,
    MSG_PARAM_RECIPIENT_DISPLAY_NAME,
    MSG_PARAM_RECIPIENT_ID,
    MSG_PARAM_RECIPIENT_USER_NAME,
    MSG_PARAM_RECIPIENT_NAME,
    MSG_PARAM_SUB_PLAN,
    MSG_PARAM_SUB_PLAN_NAME,
    MSG_PARAM_VIEWERCOUNT,
    MSG_PARAM_RITUAL_NAME,
    MSG_PARAM_SENDER_COUNT,
    MSG_PARAM_BITS_AMOUNT,
    MSG_PARAM_MIN_CHEER_AMOUNT,
    MSG_PARAM_SELECTED_COUNT,

    EMOTE_ONLY,
    FOLLOWERS_ONLY,
    RITUALS,
    TARGET_MSG_ID,
    TARGET_USER_ID,

    THREAD_ID
//    MERCURY,
//    SENT_TS,
    ;

    companion object {
        fun getTag(tag: String): Tag {
            return Tag.valueOf(tag.replace("-", "_").toUpperCase())
        }
    }
}