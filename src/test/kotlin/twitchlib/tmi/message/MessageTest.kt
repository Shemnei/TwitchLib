package twitchlib.tmi.message

import javafx.scene.paint.Color
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.*

internal class MessageTest {

    @Test
    fun testParseCLEARCHATMessage() {
        val m = Message.parse("@ban-duration=1;ban-reason=Follow\\sthe\\srules :tmi.twitch.tv CLEARCHAT #dallas :ronni")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(2, m.tagCompound!!.tags.size)
        // ban duration
        assertNotNull(m.tagCompound!!.banDuration)
        assertEquals(Duration.ofSeconds(1), m.tagCompound!!.banDuration)
        // ban reason
        assertNotNull(m.tagCompound!!.banReason)
        assertEquals("Follow the rules", m.tagCompound!!.banReason)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.CLEARCHAT, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("dallas", m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("ronni", m.message)
    }

    @Test
    fun testParseGLOBALUSERSTATEMessage() {
        val m = Message.parse("@color=#0D4200;display-name=dallas;emote-sets=0,33,50,237,793,2126,3517,4578,5569,9400,10337,12239;turbo=0;user-id=1337;user-type=admin :tmi.twitch.tv GLOBALUSERSTATE")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(6, m.tagCompound!!.tags.size)
        // color
        assertNotNull(m.tagCompound!!.color)
        assertEquals(Color.valueOf("#0D4200"), m.tagCompound!!.color)
        // display name
        assertNotNull(m.tagCompound!!.displayName)
        assertEquals("dallas", m.tagCompound!!.displayName)
        // emote sets
        assertNotNull(m.tagCompound!!.emoteSets)
        assertEquals(listOf(0, 33, 50, 237, 793, 2126, 3517, 4578, 5569, 9400, 10337, 12239), m.tagCompound!!.emoteSets)
        // turbo
        assertNotNull(m.tagCompound!!.turbo)
        assertFalse(m.tagCompound!!.turbo!!)
        // user id
        assertNotNull(m.tagCompound!!.userId)
        assertEquals(1337L, m.tagCompound!!.userId)
        // user type
        assertNotNull(m.tagCompound!!.userType)
        assertEquals(UserType.ADMIN, m.tagCompound!!.userType)
    }

    @Test
    fun testParsePRIVMSGMessage() {
        val m = Message.parse("@badges=global_mod/1,turbo/1;color=#0D4200;display-name=dallas;emotes=25:0-4,12-16/1902:6-10;id=b34ccfc7-4977-403a-8a94-33c6bac34fb8;mod=0;room-id=1337;subscriber=0;tmi-sent-ts=1507246572675;turbo=1;user-id=1337;user-type=global_mod :ronni!ronni@ronni.tmi.twitch.tv PRIVMSG #dallas :Kappa Keepo Kappa")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(12, m.tagCompound!!.tags.size)
        // badges
        assertNotNull(m.tagCompound!!.badges)
        assertEquals(2, m.tagCompound!!.badges!!.size)
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.GLOBAL_MOD, 1)))
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.TURBO, 1)))
        // color
        assertNotNull(m.tagCompound!!.color)
        assertEquals(Color.valueOf("#0D4200"), m.tagCompound!!.color)
        // display name
        assertNotNull(m.tagCompound!!.displayName)
        assertEquals("dallas", m.tagCompound!!.displayName)
        // emotes
        assertNotNull(m.tagCompound!!.emotes)
        assertEquals(2, m.tagCompound!!.emotes!!.size)
        assertTrue(m.tagCompound!!.emotes!!.containsKey(25))
        assertEquals(listOf(IntRange(0, 4), IntRange(12, 16)), m.tagCompound!!.emotes!![25])
        assertTrue(m.tagCompound!!.emotes!!.containsKey(1902))
        assertEquals(listOf(IntRange(6, 10)), m.tagCompound!!.emotes!![1902])
        // id
        assertNotNull(m.tagCompound!!.id)
        assertEquals("b34ccfc7-4977-403a-8a94-33c6bac34fb8", m.tagCompound!!.id)
        // mod
        assertNotNull(m.tagCompound!!.mod)
        assertFalse(m.tagCompound!!.mod!!)
        // room id
        assertNotNull(m.tagCompound!!.roomId)
        assertEquals(1337L, m.tagCompound!!.roomId)
        // subscriber
        assertNotNull(m.tagCompound!!.subscriber)
        assertFalse(m.tagCompound!!.subscriber!!)
        // tmi sent ts
        assertNotNull(m.tagCompound!!.tmiSentTS)
        assertEquals(1507246572675L, m.tagCompound!!.tmiSentTS)
        // turbo
        assertNotNull(m.tagCompound!!.turbo)
        assertTrue(m.tagCompound!!.turbo!!)
        // user id
        assertNotNull(m.tagCompound!!.userId)
        assertEquals(1337L, m.tagCompound!!.userId)
        // user type
        assertNotNull(m.tagCompound!!.userType)
        assertEquals(UserType.GLOBAL_MOD, m.tagCompound!!.userType)

        // sender
        assertEquals("ronni", m.sender)

        // command
        assertEquals(Command.PRIVMSG, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("dallas", m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("Kappa Keepo Kappa", m.message)
    }

    @Test
    fun testParseCHEERMessage() {
        val m = Message.parse("@badges=staff/1,bits/1000;bits=100;color=;display-name=dallas;emotes=;id=b34ccfc7-4977-403a-8a94-33c6bac34fb8;mod=0;room-id=1337;subscriber=0;tmi-sent-ts=1507246572675;turbo=1;user-id=1337;user-type=staff :ronni!ronni@ronni.tmi.twitch.tv PRIVMSG #dallas :cheer100")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(13, m.tagCompound!!.tags.size)
        // badges
        assertNotNull(m.tagCompound!!.badges)
        assertEquals(2, m.tagCompound!!.badges!!.size)
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.STAFF, 1)))
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.BITS, 1000)))
        // bits
        assertNotNull(m.tagCompound!!.bits)
        assertEquals(100, m.tagCompound!!.bits!!)
        // color
        assertNull(m.tagCompound!!.color)
        // display name
        assertNotNull(m.tagCompound!!.displayName)
        assertEquals("dallas", m.tagCompound!!.displayName)
        // emotes
        assertNull(m.tagCompound!!.emotes)
        // id
        assertNotNull(m.tagCompound!!.id)
        assertEquals("b34ccfc7-4977-403a-8a94-33c6bac34fb8", m.tagCompound!!.id)
        // mod
        assertNotNull(m.tagCompound!!.mod)
        assertFalse(m.tagCompound!!.mod!!)
        // room id
        assertNotNull(m.tagCompound!!.roomId)
        assertEquals(1337L, m.tagCompound!!.roomId)
        // subscriber
        assertNotNull(m.tagCompound!!.subscriber)
        assertFalse(m.tagCompound!!.subscriber!!)
        // tmi sent ts
        assertNotNull(m.tagCompound!!.tmiSentTS)
        assertEquals(1507246572675L, m.tagCompound!!.tmiSentTS)
        // turbo
        assertNotNull(m.tagCompound!!.turbo)
        assertTrue(m.tagCompound!!.turbo!!)
        // user id
        assertNotNull(m.tagCompound!!.userId)
        assertEquals(1337L, m.tagCompound!!.userId)
        // user type
        assertNotNull(m.tagCompound!!.userType)
        assertEquals(UserType.STAFF, m.tagCompound!!.userType)

        // sender
        assertEquals("ronni", m.sender)

        // command
        assertEquals(Command.PRIVMSG, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("dallas", m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("cheer100", m.message)
    }

    @Test
    fun testParseROOMSTATEMessage() {
        val m = Message.parse("@broadcaster-lang=en;r9k=0;slow=10;subs-only=0 :tmi.twitch.tv ROOMSTATE #dallas")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(4, m.tagCompound!!.tags.size)
        // broadcaster lang
        assertNotNull(m.tagCompound!!.broadcasterLang)
        assertEquals(Locale.ENGLISH, m.tagCompound!!.broadcasterLang)
        // r9k
        assertNotNull(m.tagCompound!!.r9k)
        assertFalse(m.tagCompound!!.r9k!!)
        // slow
        assertNotNull(m.tagCompound!!.slow)
        assertEquals(Duration.ofSeconds(10), m.tagCompound!!.slow!!)
        // subs only
        assertNotNull(m.tagCompound!!.subsOnly)
        assertFalse(m.tagCompound!!.subsOnly!!)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.ROOMSTATE, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("dallas", m.receiver!!)
    }

    @Test
    fun testParseUSERNOTICE_RESUB_Message() {
        val m = Message.parse("@badges=staff/1,broadcaster/1,turbo/1;color=#008000;display-name=ronni;emotes=;id=db25007f-7a18-43eb-9379-80131e44d633;login=ronni;mod=0;msg-id=resub;msg-param-months=6;msg-param-sub-plan=Prime;msg-param-sub-plan-name=Prime;room-id=1337;subscriber=1;system-msg=ronni\\shas\\ssubscribed\\sfor\\s6\\smonths!;tmi-sent-ts=1507246572675;turbo=1;user-id=1337;user-type=staff :tmi.twitch.tv USERNOTICE #dallas :Great stream -- keep it up!")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(18, m.tagCompound!!.tags.size)
        // badges
        assertNotNull(m.tagCompound!!.badges)
        assertEquals(3, m.tagCompound!!.badges!!.size)
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.STAFF, 1)))
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.BROADCASTER, 1)))
        assertTrue(m.tagCompound!!.badges!!.contains(Pair(Badge.TURBO, 1)))
        // login
        assertNotNull(m.tagCompound!!.login)
        assertEquals("ronni", m.tagCompound!!.login)
        // message id
        assertNotNull(m.tagCompound!!.msgId)
        assertEquals(NoticeType.RESUB, m.tagCompound!!.msgId)
        // param months
        assertNotNull(m.tagCompound!!.paramMonths)
        assertEquals(6, m.tagCompound!!.paramMonths)
        // param sub plan
        assertNotNull(m.tagCompound!!.paramSubPlan)
        assertEquals(SubPlan.PRIME, m.tagCompound!!.paramSubPlan)
        // param sub plan name
        assertNotNull(m.tagCompound!!.paramSubPlanName)
        assertEquals("Prime", m.tagCompound!!.paramSubPlanName)
        // system message
        assertNotNull(m.tagCompound!!.systemMessage)
        assertEquals("ronni has subscribed for 6 months!", m.tagCompound!!.systemMessage)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.USERNOTICE, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("dallas", m.receiver!!)

        // message
        assertNotNull(m.message)
        assertEquals("Great stream -- keep it up!", m.message)
    }

    @Test
    fun testParseUSERNOTICE_GIFTSUB_Message() {
        val m = Message.parse("@badges=staff/1,premium/1;color=#0000FF;display-name=TWW2;emotes=;id=e9176cd8-5e22-4684-ad40-ce53c2561c5e;login=tww2;mod=0;msg-id=subgift;msg-param-months=1;msg-param-recipient-display-name=Mr_Woodchuck;msg-param-recipient-id=89614178;msg-param-recipient-name=mr_woodchuck;msg-param-sub-plan-name=House\\sof\\sNyoro~n;msg-param-sub-plan=1000;room-id=19571752;subscriber=0;system-msg=TWW2\\sgifted\\sa\\sTier\\s1\\ssub\\sto\\sMr_Woodchuck!;tmi-sent-ts=1521159445153;turbo=0;user-id=13405587;user-type=staff :tmi.twitch.tv USERNOTICE #forstycup")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(21, m.tagCompound!!.tags.size)
        // message id
        assertNotNull(m.tagCompound!!.msgId)
        assertEquals(NoticeType.SUBGIFT, m.tagCompound!!.msgId)
        // param months
        assertNotNull(m.tagCompound!!.paramMonths)
        assertEquals(1, m.tagCompound!!.paramMonths)
        // param recipient display name
        assertNotNull(m.tagCompound!!.paramRecipientDisplayName)
        assertEquals("Mr_Woodchuck", m.tagCompound!!.paramRecipientDisplayName)
        // param recipient id
        assertNotNull(m.tagCompound!!.paramRecipientId)
        assertEquals(89614178L, m.tagCompound!!.paramRecipientId)
        // param recipient name
        assertNotNull(m.tagCompound!!.paramRecipientName)
        assertEquals("mr_woodchuck", m.tagCompound!!.paramRecipientName)
        // param sub plan
        assertNotNull(m.tagCompound!!.paramSubPlan)
        assertEquals(SubPlan._1000, m.tagCompound!!.paramSubPlan)
        // param sub plan name
        assertNotNull(m.tagCompound!!.paramSubPlanName)
        assertEquals("House of Nyoro~n", m.tagCompound!!.paramSubPlanName)
        // system message
        assertNotNull(m.tagCompound!!.systemMessage)
        assertEquals("TWW2 gifted a Tier 1 sub to Mr_Woodchuck!", m.tagCompound!!.systemMessage)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.USERNOTICE, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("forstycup", m.receiver!!)
    }

    @Test
    fun testParseUSERNOTICE_RAID_Message() {
        val m = Message.parse("@badges=turbo/1;color=#9ACD32;display-name=TestChannel;emotes=;id=3d830f12-795c-447d-af3c-ea05e40fbddb;login=testchannel;mod=0;msg-id=raid;msg-param-displayName=TestChannel;msg-param-login=testchannel;msg-param-viewerCount=15;room-id=56379257;subscriber=0;system-msg=15\\sraiders\\sfrom\\sTestChannel\\shave\\sjoined\\n!;tmi-sent-ts=1507246572675;tmi-sent-ts=1507246572675;turbo=1;user-id=123456;user-type= :tmi.twitch.tv USERNOTICE #othertestchannel")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(18, m.tagCompound!!.tags.size)
        // message id
        assertNotNull(m.tagCompound!!.msgId)
        assertEquals(NoticeType.RAID, m.tagCompound!!.msgId)
        // param login
        assertNotNull(m.tagCompound!!.paramLogin)
        assertEquals("testchannel", m.tagCompound!!.paramLogin)
        // param viewer count
        assertNotNull(m.tagCompound!!.paramViewerCount)
        assertEquals(15, m.tagCompound!!.paramViewerCount)
        // system message
        assertNotNull(m.tagCompound!!.systemMessage)
        assertEquals("15 raiders from TestChannel have joined\\n!", m.tagCompound!!.systemMessage)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.USERNOTICE, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("othertestchannel", m.receiver!!)
    }

    @Test
    fun testParseUSERSTATEMessage() {
        val m = Message.parse("@color=#0D4200;display-name=ronni;emote-sets=0,33,50,237,793,2126,3517,4578,5569,9400,10337,12239;mod=1;subscriber=1;turbo=1;user-type=staff :tmi.twitch.tv USERSTATE #dallas")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(7, m.tagCompound!!.tags.size)
        // color
        assertNotNull(m.tagCompound!!.color)
        assertEquals(Color.valueOf("#0D4200"), m.tagCompound!!.color)
        // display name
        assertNotNull(m.tagCompound!!.displayName)
        assertEquals("ronni", m.tagCompound!!.displayName)
        // emote sets
        assertNotNull(m.tagCompound!!.emoteSets)
        assertEquals(listOf(0, 33, 50, 237, 793, 2126, 3517, 4578, 5569, 9400, 10337, 12239), m.tagCompound!!.emoteSets)
        // mod
        assertNotNull(m.tagCompound!!.turbo)
        assertTrue(m.tagCompound!!.turbo!!)
        // subscriber
        assertNotNull(m.tagCompound!!.subscriber)
        assertTrue(m.tagCompound!!.subscriber!!)
        // turbo
        assertNotNull(m.tagCompound!!.turbo)
        assertTrue(m.tagCompound!!.turbo!!)
        // user type
        assertNotNull(m.tagCompound!!.userType)
        assertEquals(UserType.STAFF, m.tagCompound!!.userType)
    }

    @Test
    fun testParseHOSTTARGET_START_Message() {
        val m = Message.parse(":tmi.twitch.tv HOSTTARGET #hosting_channel <channel> 15")
        // tag compound
        assertNull(m.tagCompound)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.HOSTTARGET, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("hosting_channel", m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("<channel> 15", m.message)
    }

    @Test
    fun testParseHOSTTARGET_END_Message() {
        val m = Message.parse(":tmi.twitch.tv HOSTTARGET #hosting_channel :- 15")
        // tag compound
        assertNull(m.tagCompound)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.HOSTTARGET, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("hosting_channel", m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("- 15", m.message)
    }

    @Test
    fun testParseNOTICEMessage() {
        val m = Message.parse("@msg-id=slow_off :tmi.twitch.tv NOTICE #dallas :This room is no longer in slow mode.")
        // tag compound
        assertNotNull(m.tagCompound)
        assertEquals(m.tagCompound!!.tags.size, 1)
        // message id
        assertNotNull(m.tagCompound!!.msgId)
        assertEquals(NoticeType.SLOW_OFF, m.tagCompound!!.msgId)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.NOTICE, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("dallas", m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("This room is no longer in slow mode.", m.message)
    }

    // FIXME: 26.05.2018 replace own example with real one
    @Test
    fun testParseRECONNECTMessage() {
        val m = Message.parse(":tmi.twitch.tv RECONNECT")
        // tag compound
        assertNull(m.tagCompound)

        // sender
        assertEquals("tmi.twitch.tv", m.sender)

        // command
        assertEquals(Command.RECONNECT, m.command)
    }

    @Test
    fun testParseJOINMessage() {
        val m = Message.parse(":ronni!ronni@ronni.tmi.twitch.tv JOIN #chatrooms:44322889:04e762ec-ce8f-4cbc-b6a3-ffc871ab53da")
        // tag compound
        assertNull(m.tagCompound)

        // sender
        assertEquals("ronni", m.sender)

        // command
        assertEquals(Command.JOIN, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("chatrooms:44322889:04e762ec-ce8f-4cbc-b6a3-ffc871ab53da", m.receiver)
    }

    @Test
    fun testParsePINGMessage() {
        val m = Message.parse("PING :tmi.twitch.tv")
        // tag compound
        assertNull(m.tagCompound)

        // sender
        assertTrue(m.sender.isEmpty())

        // command
        assertEquals(Command.PING, m.command)

        // receiver
        assertNull(m.receiver)

        // message
        assertNotNull(m.message)
        assertEquals("tmi.twitch.tv", m.message)
    }

    @Test
    fun testParseCONNECTIONMessage() {
        var m = Message.parse(":tmi.twitch.tv 001 <user> :Welcome, GLHF!")
        // command
        assertEquals(Command._001, m.command)

        // receiver
        assertNotNull(m.receiver)
        assertEquals("<user>", m.receiver)

        m = Message.parse(":tmi.twitch.tv 002 <user> :Your host is tmi.twitch.tv")
        // command
        assertEquals(Command._002, m.command)

        m = Message.parse(":tmi.twitch.tv 003 <user> :This server is rather new")
        // command
        assertEquals(Command._003, m.command)

        m = Message.parse(":tmi.twitch.tv 004 <user> :-")
        // command
        assertEquals(Command._004, m.command)

        m = Message.parse(":tmi.twitch.tv 375 <user> :-")
        // command
        assertEquals(Command._375, m.command)

        m = Message.parse(":tmi.twitch.tv 372 <user> :You are in a maze of twisty passages.")
        // command
        assertEquals(Command._372, m.command)

        m = Message.parse(":tmi.twitch.tv 376 <user> :>")
        // command
        assertEquals(Command._376, m.command)
    }

}