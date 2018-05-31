package twitchlib.tmi

import twitchlib.tmi.message.MessageType
import twitchlib.tmi.message.Message
import java.io.BufferedReader
import java.io.Closeable
import java.io.PrintWriter
import java.net.Socket
import javax.net.ssl.SSLSocketFactory

class IRCConnection(
        val host: String = "irc.chat.twitch.tv",
        val port: Int = 443,
        val ssl: Boolean = true,
        val membership: Boolean = true,
        val tags: Boolean = true,
        val commands: Boolean = true
) : Closeable {
    private lateinit var socket: Socket
    private lateinit var br: BufferedReader
    private lateinit var pw: PrintWriter

    var connected: Boolean = false

    fun connect(user: String, oauth: String) {
        if (connected)
            throw IllegalStateException("IRCConnection is already connected")

        socket = if (ssl) {
            SSLSocketFactory.getDefault().createSocket(host, port)
        } else {
            Socket(host, port)
        }
        pw = PrintWriter(socket.getOutputStream(), true)
        br = socket.getInputStream().bufferedReader()

        val oauthPre = if (oauth.startsWith("oauth:")) "" else "oauth:"

        pw.println("PASS ${oauthPre + oauth}")
        pw.println("NICK $user")

        if (membership)
            pw.println("CAP REQ :twitch.tv/membership")
        if (tags)
            pw.println("CAP REQ :twitch.tv/tags")
        if (commands)
            pw.println("CAP REQ :twitch.tv/commands")

        connected = true
    }

    override fun close() {
        pw.flush()
        pw.close()
        br.close()
        socket.close()
        connected = false
    }

    // blocking - waits for next message
    fun recv(): Message {
        return Message.parse(br.readLine())
    }

    fun sendRaw(raw: String) {
        if (!connected)
            throw IllegalStateException("IRCConnection is not connected")
        pw.println(raw)
    }

    fun join(channel: String) {
        sendRaw("${MessageType.JOIN} #$channel")
    }

    fun part(channel: String) {
        sendRaw("${MessageType.PART} #$channel")
    }

    fun say(channel: String, message: String) {
        sendRaw("${MessageType.PRIVMSG} #$channel :$message")
    }

    // could also send to #jtv
    fun whisper(user: String, message: String) {
        sendRaw("${MessageType.PRIVMSG} #$user :/w $user $message")
    }

    fun pong(pingMessage: String = "tmi.twitch.tv") {
        sendRaw("${MessageType.PONG} :$pingMessage")
    }
}