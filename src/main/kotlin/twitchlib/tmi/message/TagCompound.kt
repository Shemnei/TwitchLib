package twitchlib.tmi.message

import javafx.scene.paint.Color
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TagCompound private constructor(
        val raw: String
){

    val tags: Map<Tag, String?> by lazy {
        try {
            raw.split(";").associate { val pair = it.split("="); Pair(Tag.getTag(pair[0]), pair.getOrNull(1)) }
        } catch (e: Exception) {
            System.err.println(raw)
            e.printStackTrace()
            mapOf<Tag, String?>()
        }
    }

    enum class Badge {ADMIN, BITS, BROADCASTER, GLOBAL_MOD, MODERATOR, SUBSCRIBER, STAFF, TURBO}
    val badges: List<Pair<Badge, Int>>? by lazy {
        tags[Tag.BITS]
                ?.split(",")
                ?.map {
                    val split = it.split("/");
                    Pair(Badge.valueOf(it.toUpperCase()), split.getOrNull(1)?.toInt() ?: 0)
                }
    }

    val banDuration: Duration? by lazy {
        tags[Tag.BAN_DURATION]?.let { Duration.ofSeconds(it.toLong()) }
    }

    val banReason: String? by lazy {
        tags[Tag.BAN_REASON]
    }

    val bits: Int? by lazy {
        tags[Tag.BITS]?.toInt()
    }

    val broadcasterLang: Locale? by lazy {
        tags[Tag.BROADCASTER_LANG]?.let { Locale(it) }
    }

    // TODO: 25.05.2018 Maybe replace with own wrapper class
    val color: Color? by lazy {
        tags[Tag.COLOR]?.let { Color.valueOf(it) }
    }

    val displayName: String? by lazy {
        tags[Tag.DISPLAY_NAME]
    }

    // TODO: 25.05.2018 Replace with emote class ?
    // TODO: 25.05.2018 Some Cleanup?
    val emote: Map<Int, List<IntRange>>? by lazy {
        tags[Tag.EMOTES]
                ?.split("/")
                ?.associate {
                    // emoteID:r1-r2,r3-r4
                    val split = it.split(":")
                    split[0].toInt() to
                            split[2]
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

    enum class NoticeType {
        // tag
        SUB, RESUB, SUBGIFT, RAID, RITUAL,
        // system
        ALREADY_BANNED, ALREADY_EMOTE_ONLY_OFF, ALREADY_EMOTE_ONLY_ON, ALREADY_R9K_OFF, ALREADY_R9K_ON, BAN_SUCCESS, BAD_UNBAN_NO_BAN, EMOTE_ONLY_OFF, EMOTE_ONLY_ON, MSG_CHANNEL_SUSPENDED, MSG_ROOM_NOT_FOUND, NO_PERMISSION, R9K_OFF, R9K_ON, SLOW_OFF, SLOW_ON, TIMEOUT_SUCCESS, UNBAN_SUCCESS, UNRECOGNIZED_CMD, UNSUPPORTED_CHATROOMS_CMD;
    }
    val msgId: NoticeType? by lazy {
        tags[Tag.MSG_ID]?.let { NoticeType.valueOf(it.toUpperCase()) }
    }

    val r9k: Boolean? by lazy {
        tags[Tag.R9K]?.let { it == "1" }
    }

    val roomId: Int? by lazy {
        tags[Tag.ROOM_ID]?.toInt()
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
        tags[Tag.SYSTEM_MSG]
    }

    val sentTS: Long? by lazy {
        tags[Tag.TMI_SENT_TS]?.toLong()
    }

    val sentLocalDateTime: LocalDateTime? by lazy {
        sentTS?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }

    val turbo: Boolean? by lazy {
        tags[Tag.TURBO]?.let { it == "1" }
    }

    val userId: Int? by lazy {
        tags[Tag.USER_ID]?.toInt()
    }

    enum class UserType { EMPTY, MOD, GLOBAL_MOD, ADMIN, STAFF }
    val userType: UserType? by lazy {
        tags[Tag.USER_TYPE]?.let { UserType.valueOf(it.toUpperCase()) }
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

    val paramRecipientId: Int? by lazy {
        tags[Tag.MSG_PARAM_RECIPIENT_ID]?.toInt()
    }

    val paramRecipientUserName: String? by lazy {
        tags[Tag.MSG_PARAM_RECIPIENT_USER_NAME]
    }

    enum class SubPlan {
        PRIME, _1000, _2000, _3000;
        companion object {
            val numberRegex = Regex("""[0-9]+""")
            fun getPlan(plan: String): SubPlan {
                val pre = if (plan.matches(numberRegex)) "_" else ""
                return SubPlan.valueOf(pre + plan)
            }
        }
    }
    val paramSubPlan: SubPlan? by lazy {
        tags[Tag.MSG_PARAM_SUB_PLAN]?.let { SubPlan.getPlan(it) }
    }

    val paramSubPlanName: String? by lazy {
        tags[Tag.MSG_PARAM_SUB_PLAN_NAME]
    }

    val paramViewerCount: Int? by lazy {
        tags[Tag.MSG_PARAM_VIEWERCOUNT]?.toInt()
    }

    val paramRitualName: String? by lazy {
        tags[Tag.MSG_PARAM_RITUAL_NAME]
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

    val targetUserId: Int? by lazy {
        tags[Tag.TARGET_USER_ID]?.toInt()
    }

    val targetMsgId: String? by lazy {
        tags[Tag.TARGET_MSG_ID]
    }

    companion object {
        fun parse(raw: String): TagCompound {
            return TagCompound(raw)
        }
    }

}