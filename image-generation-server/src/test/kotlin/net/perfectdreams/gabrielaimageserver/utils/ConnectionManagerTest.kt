package net.perfectdreams.gabrielaimageserver.utils

import net.perfectdreams.gabrielaimageserver.webserver.utils.ConnectionManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class
ConnectionManagerTest {
    private val trustedUrls = listOf(
        "loritta.website",
    )

    private val manager = ConnectionManager(trustedUrls)

    @Test
    fun `test trusted urls`() {
        assertThat(manager.isTrusted("https://loritta.website/")).isTrue
        assertThat(manager.isTrusted("https://canary.loritta.website/")).isTrue
        assertThat(manager.isTrusted("https://example.com")).isFalse
        assertThat(manager.isTrusted("https://example.com/owouwuowouwu?uwu=owo")).isFalse
    }
}