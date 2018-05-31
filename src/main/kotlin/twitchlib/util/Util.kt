package twitchlib.util

import java.awt.Color

fun Color.toHex(): String {
    val r = this.red.toHex().padStart(2, '0')
    val g = this.green.toHex().padStart(2, '0')
    val b = this.blue.toHex().padStart(2, '0')
    return "#$r$g$b"
}

fun Int.toHex(): String {
    return Integer.toHexString(this)
}