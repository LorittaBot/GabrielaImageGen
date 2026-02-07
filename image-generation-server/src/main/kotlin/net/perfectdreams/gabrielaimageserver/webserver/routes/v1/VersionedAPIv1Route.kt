package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.github.oshai.kotlinlogging.KLogger
import net.perfectdreams.gabrielaimageserver.webserver.routes.BaseRoute
import java.util.*

abstract class VersionedAPIv1Route(path: String) : BaseRoute(
    "/api/v1$path"
) {
    /**
     * Logs the request and generates a random UUID to be easier to track requests issues
     *
     * @param logger the logger of the caller
     * @param block  the generator code
     */
    suspend fun withRequest(logger: KLogger, block: suspend (UUID) -> (Unit)) {
        val uniqueId = UUID.randomUUID()

        logger.info { "Received request with UUID: $uniqueId" }
        block.invoke(uniqueId)
        logger.info { "Sent request with UUID: $uniqueId (yay!)" }
    }
}