package net.perfectdreams.imageserver.routes

import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KLogger
import mu.KotlinLogging
import net.perfectdreams.imageserver.utils.Constants
import net.perfectdreams.imageserver.utils.extensions.getImageDataContext
import java.util.*

abstract class VersionedAPIRoute(path: String) : BaseRoute(
    "/api/${Constants.API_VERSION}$path"
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