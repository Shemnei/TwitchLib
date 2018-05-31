package twitchlib.tmi

import twitchlib.util.toHex
import java.awt.Color
import java.time.Duration

object CommandBuilder {
    // general
    fun color(color: TwitchColor): String {
        return "/color ${color.name}"
    }

    // turbo only
    fun color(color: Color): String {
        return "/color ${color.toHex()}"
    }

    fun disconnect(): String {
        return "/disconnect"
    }

    fun help(cmd: String?): String {
        return "/help ${cmd ?: ""}"
    }

    fun me(msg: String): String {
        return "/me $msg"
    }

    fun mods(): String {
        return "/mods"
    }

    fun w(user: String, msg: String): String {
        return "/w $user $msg"
    }

    // user moderation
    fun ban(user: String): String {
        return "/ban $user"
    }

    fun timeout(user: String, duration: Duration = Duration.ofSeconds(600)): String {
        val secs = duration.seconds
        if (secs > 1209600)
            throw IllegalArgumentException("Max duration is 1209600 seconds")
        return "/timeout $user $secs"
    }

    fun unban(user: String): String {
        return "/unban $user"
    }

    fun untimeout(user: String): String {
        return "/untimeout $user"
    }

    // room moderation
    fun emoteOnly(on: Boolean = true): String {
        return if (on) "/emoteonly" else "/emoteonlyoff"
    }

    fun r9k(on: Boolean = true): String {
        return if (on) "/r9kbeta" else "/r9kbetaoff"
    }

    fun slow(duration: Duration = Duration.ofSeconds(120)): String {
        return "/slow ${duration.seconds}"
    }

    fun slowOff(): String {
        return "/slowoff"
    }

    // FIXME: 28.05.2018 twitch only accepts minute strings under one day
    fun followers(minFollowDuration: Duration? = Duration.ofMinutes(30)): String {
        minFollowDuration?.let {
            if (it >= Duration.ofDays(30 * 3))
                throw IllegalArgumentException("Max duration is 90 days")
        }
        val timeString = if (minFollowDuration == null) "" else "${minFollowDuration.toMinutes()}m"
        return "/followers $timeString"
    }

    fun followersOff(): String {
        return "/followersoff"
    }

    fun clear(): String {
        return "/clear"
    }

    // broadcaster
    fun commercial(duration: Duration? = Duration.ofMinutes(1)): String {
        return "/commercial ${duration?.seconds}"
    }

    fun host(channel: String, tagLine: String?): String {
        tagLine?.let {
            if (it.length > 40)
                throw IllegalArgumentException("Max tagLine length is 40 characters")
        }
        return "/host $channel $tagLine"
    }

    fun unhost(): String {
        return "/unhost"
    }

    // TODO: 28.05.2018 raid unraid
}

enum class TwitchColor {
    Blue, BlueViolet, CadetBlue, Chocolate, Coral, DodgerBlue, Firebrick, GoldenRod, Green, HotPink, OrangeRed, Red, SeaGreen, SpringGreen, YellowGreen
}