package twitchlib.tmi.message

import javafx.scene.paint.Color
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TagCompound private constructor(
        val raw: String
) {

    val tags: Map<Tag, String?> by lazy {
        try {
            raw.split(";")
                    .map {
                        val split = it.split("=")
                        Pair(split.first(), split.getOrNull(1))
                    }
                    .associate { Tag.getTag(it.first) to if (it.second?.isBlank() == true) null else it.second }
        } catch (e: Exception) {
            // TODO: 28.05.2018 handle
            System.err.println(raw)
            e.printStackTrace()
            mapOf<Tag, String?>()
        }
    }

    val badges: List<Pair<Badge, Int>>? by lazy {
        tags[Tag.BADGES]
                ?.split(",")
                ?.filter { !it.isBlank() }
                ?.map {
                    val split = it.split("/")
                    Pair(Badge.getBadge(split.first()), split.getOrNull(1)?.toInt() ?: 0)
                }
    }

    val banDuration: Duration? by lazy {
        tags[Tag.BAN_DURATION]?.let { Duration.ofSeconds(it.toLong()) }
    }

    val banReason: String? by lazy {
        tags[Tag.BAN_REASON]?.spaceEscapedToNormal()
    }

    val bits: Int? by lazy {
        tags[Tag.BITS]?.toInt()
    }

    val broadcasterLang: Locale? by lazy {
        tags[Tag.BROADCASTER_LANG]?.let { Locale(it) }
    }

    val color: Color? by lazy {
        tags[Tag.COLOR]?.let { Color.valueOf(it) }
    }

    val displayName: String? by lazy {
        tags[Tag.DISPLAY_NAME]
    }

    // TODO: 25.05.2018 Some Cleanup?
    val emotes: Map<Int, List<IntRange>>? by lazy {
        tags[Tag.EMOTES]
                ?.split("/")
                ?.filter { !it.isBlank() }
                ?.associate {
                    // emoteID:r1-r2,r3-r4
                    val split = it.split(":")
                    split[0].toInt() to
                            split[1]
                                    .split(",")
                                    .map {
                                        val range = it.split("-");
                                        IntRange(range[0].toInt(), range[1].toInt())
                                    }
                }
    }

    val emoteSets: List<Int>? by lazy {
        tags[Tag.EMOTE_SETS]
                ?.split(",")
                ?.map { it.toInt() }
    }

    val id: String? by lazy {
        tags[Tag.ID]
    }

    val login: String? by lazy {
        tags[Tag.LOGIN]
    }

    val mod: Boolean? by lazy {
        tags[Tag.MOD]?.let { it == "1" }
    }

    val msgId: NoticeType? by lazy {
        tags[Tag.MSG_ID]?.let { NoticeType.getNoticeType(it) }
    }

    val r9k: Boolean? by lazy {
        tags[Tag.R9K]?.let { it == "1" }
    }

    val roomId: Long? by lazy {
        tags[Tag.ROOM_ID]?.toLong()
    }

    val slow: Duration? by lazy {
        tags[Tag.SLOW]?.let { Duration.ofSeconds(it.toLong()) }
    }

    val subscriber: Boolean? by lazy {
        tags[Tag.SUBSCRIBER]?.let { it == "1" }
    }

    val subsOnly: Boolean? by lazy {
        tags[Tag.SUBS_ONLY]?.let { it == "1" }
    }

    val systemMessage: String? by lazy {
        tags[Tag.SYSTEM_MSG]?.spaceEscapedToNormal()
    }

    val tmiSentTS: Long? by lazy {
        tags[Tag.TMI_SENT_TS]?.toLong()
    }

    val tmiSentLocalDateTime: LocalDateTime? by lazy {
        tmiSentTS?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }

    val turbo: Boolean? by lazy {
        tags[Tag.TURBO]?.let { it == "1" }
    }

    val userId: Long? by lazy {
        tags[Tag.USER_ID]?.toLong()
    }

    val userType: UserType? by lazy {
        tags[Tag.USER_TYPE]?.let { UserType.getUserType(it) }
    }

    val paramDisplayName: String? by lazy {
        tags[Tag.MSG_PARAM_DISPLAYNAME]
    }

    val paramLogin: String? by lazy {
        tags[Tag.MSG_PARAM_LOGIN]
    }

    val paramMonths: Int? by lazy {
        tags[Tag.MSG_PARAM_MONTHS]?.toInt()
    }

    val paramRecipientDisplayName: String? by lazy {
        tags[Tag.MSG_PARAM_RECIPIENT_DISPLAY_NAME]
    }

    val paramRecipientId: Long? by lazy {
        tags[Tag.MSG_PARAM_RECIPIENT_ID]?.toLong()
    }

    val paramRecipientName: String? by lazy {
        tags[Tag.MSG_PARAM_RECIPIENT_NAME]
    }

    val paramRecipientUserName: String? by lazy {
        tags[Tag.MSG_PARAM_RECIPIENT_USER_NAME]
    }

    val paramSubPlan: SubPlan? by lazy {
        tags[Tag.MSG_PARAM_SUB_PLAN]?.let { SubPlan.getPlan(it) }
    }

    val paramSubPlanName: String? by lazy {
        tags[Tag.MSG_PARAM_SUB_PLAN_NAME]?.spaceEscapedToNormal()
    }

    val paramViewerCount: Int? by lazy {
        tags[Tag.MSG_PARAM_VIEWERCOUNT]?.toInt()
    }

    val paramRitualName: String? by lazy {
        tags[Tag.MSG_PARAM_RITUAL_NAME]
    }

    val paramSenderCount: Int? by lazy {
        tags[Tag.MSG_PARAM_SENDER_COUNT]?.toInt()
    }

    val paramBitsAmount: Int? by lazy {
        tags[Tag.MSG_PARAM_BITS_AMOUNT]?.toInt()
    }

    val paramMinCheerAmount: Int? by lazy {
        tags[Tag.MSG_PARAM_MIN_CHEER_AMOUNT]?.toInt()
    }

    val paramSelectedCount: Int? by lazy {
        tags[Tag.MSG_PARAM_SELECTED_COUNT]?.toInt()
    }

    val emoteOnly: Boolean? by lazy {
        tags[Tag.EMOTE_ONLY]?.let { it == "1" }
    }

    val followersOnly: Duration? by lazy {
        tags[Tag.FOLLOWERS_ONLY]?.let { Duration.ofMinutes(it.toLong()) }
    }

    // TODO: 25.05.2018 Check if it really is a bool
    val rituals: Boolean? by lazy {
        tags[Tag.RITUALS]?.let { it == "1" }
    }

    val targetUserId: Long? by lazy {
        tags[Tag.TARGET_USER_ID]?.toLong()
    }

    val targetMsgId: String? by lazy {
        tags[Tag.TARGET_MSG_ID]
    }

    val threadId: String? by lazy {
        tags[Tag.THREAD_ID]
    }

    companion object {
        fun parse(raw: String): TagCompound {
            return TagCompound(raw)
        }
    }
}

enum class Badge {
    ADMIN, BITS, BROADCASTER, GLOBAL_MOD, MODERATOR, SUBSCRIBER,
    STAFF, TURBO, PREMIUM, PARTNER,

    // Special undocument
    SUB_GIFTER, CLIP_CHAMP, BATTLERITE_1, OVERWATCH_LEAGUE_INSIDER_1,
    H1Z1_1, TWITCHCON2017, BRAWLHALLA_1, _60_SECONDS_3, CUPHEAD_1, BITS_LEADER,
    STARBOUND_1, ANOMALY_WARZONE_EARTH_1;

    companion object {
        fun getBadge(badge: String): Badge {
            val normBadge = if (badge.first().isDigit()) "_$badge" else badge
            return Badge.valueOf(normBadge.toUpperCase().replace("-", "_"))
        }
    }
}

enum class NoticeType {
    // tag
    SUB,
    RESUB, SUBGIFT, RAID, RITUAL,
    // TODO: 01.06.2018 whats this
    REWARDGIFT,
    // system
    ALREADY_BANNED,
    ALREADY_EMOTE_ONLY_OFF, ALREADY_EMOTE_ONLY_ON, ALREADY_R9K_OFF, ALREADY_R9K_ON, ALREADY_SUBS_OFF,
    ALREADY_SUBS_ON, BAD_HOST_HOSTING, BAN_SUCCESS, BAD_UNBAN_NO_BAN, EMOTE_ONLY_OFF, EMOTE_ONLY_ON,
    HOST_OFF, HOST_ON, HOSTS_REMAINING, MSG_CHANNEL_SUSPENDED, R9K_OFF, R9K_ON, SLOW_OFF, SLOW_ON,
    SUBS_OFF, SUBS_ON, TIMEOUT_SUCCESS, UNBAN_SUCCESS, UNRECOGNIZED_CMD, UNSUPPORTED_CHATROOMS_CMD;

    companion object {
        fun getNoticeType(type: String): NoticeType {
            return NoticeType.valueOf(type.toUpperCase().replace("-", "_"))
        }
    }
}

enum class UserType {
    EMPTY, MOD, GLOBAL_MOD, ADMIN, STAFF;

    companion object {
        fun getUserType(type: String): UserType {
            return if (type.isBlank()) EMPTY
            else valueOf(type.toUpperCase().replace("-", "_"))
        }
    }
}

enum class SubPlan {
    PRIME, _1000, _2000, _3000;

    companion object {
        val numberRegex = Regex("""[0-9]+""")
        fun getPlan(plan: String): SubPlan {
            val pre = if (plan.matches(numberRegex)) "_" else ""
            return SubPlan.valueOf(pre + plan.toUpperCase())
        }
    }
}

fun String.spaceEscapedToNormal(): String {
    return this.replace("\\s", " ")
}