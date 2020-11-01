package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.getImageDataContext
import java.util.*

class PostCarlyAaahRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/videos/carly-aaah"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        val uniqueId = UUID.randomUUID()

        logger.info { "Received request with UUID: $uniqueId" }
        val imagesContext = call.getImageDataContext()

        val sourceImage = imagesContext.retrieveImage(0)

        val result = withContext(m.coroutineDispatcher) {
            m.generators.CARLY_AAAH_GENERATOR.generate(sourceImage)
        }

        call.respondBytes(result, ContentType.Video.MP4)
        logger.info { "Sent request with UUID: $uniqueId (yay!)" }
    }
}