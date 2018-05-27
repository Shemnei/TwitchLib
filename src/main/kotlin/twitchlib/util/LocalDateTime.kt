package twitchlib.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun LocalDateTime.zone(fromZone: ZoneId, toZone: ZoneId): LocalDateTime {
    return LocalDateTime.ofInstant(ZonedDateTime.of(this, fromZone).toInstant(), toZone)
}

fun LocalDateTime.utcToZone(toZone: ZoneId): LocalDateTime {
    return this.zone(java.time.ZoneId.of("UTC"), toZone)
}

fun LocalDateTime.utcToDefault(): LocalDateTime {
    return this.zone(ZoneId.of("UTC"), java.time.ZoneId.systemDefault())
}

fun ZonedDateTime.toLocalDateTime(toZone: ZoneId): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), toZone)
}

fun ZonedDateTime.toSystemDateTime(): LocalDateTime {
    return this.toLocalDateTime(java.time.ZoneId.systemDefault())
}