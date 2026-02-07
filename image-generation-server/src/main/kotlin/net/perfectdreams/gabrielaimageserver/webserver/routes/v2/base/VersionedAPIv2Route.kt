package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.webserver.routes.BaseRoute
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import java.util.*

abstract class VersionedAPIv2Route(path: String) : BaseRoute(
    "/api/v2$path",
) {
    companion object {
        internal val logger = KotlinLogging.logger {}
    }

    /**
     * Logs the request and generates a random UUID to be easier to track requests issues
     *
     * @param block  the generator code
     */
    suspend fun withRequest(block: suspend (UUID) -> (Unit)) {
        val uniqueId = UUID.randomUUID()

        try {
            logger.info { "Received request with UUID: $uniqueId" }
            block.invoke(uniqueId)
            logger.info { "Sent request with UUID: $uniqueId (yay!)" }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}