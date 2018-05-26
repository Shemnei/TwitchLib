package twitchlib.helix

import java.time.Duration
import java.time.Instant

class RequestLimit(
        val maxRequests: Int,
        val duration: Duration = Duration.ofMinutes(1)
) {
    private var startEpoch: Instant = Instant.now()
    private var requestSinceStart: Int = 0

    val canRequest: Boolean
        get() {
            if (timeToReset == Duration.ZERO) {
                startEpoch = Instant.now()
                requestSinceStart = 0
            }
            return if (requestSinceStart < maxRequests) {
                requestSinceStart++
                true
            } else {
                false
            }
        }

    val timeToReset: Duration
        get() {
            val diff = Instant.now().epochSecond - startEpoch.epochSecond
            return if (diff >= duration.seconds) {
                Duration.ZERO
            } else {
                Duration.ofSeconds(duration.seconds - diff)
            }
        }
}