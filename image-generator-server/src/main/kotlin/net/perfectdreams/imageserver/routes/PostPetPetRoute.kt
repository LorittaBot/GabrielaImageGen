package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import java.util.*

class PostPetPetRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/images/pet-pet"
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
            m.generators.HAND_PAT_GENERATOR.generate(sourceImage)
        }

        call.respondBytes(result, ContentType.Image.GIF)
        logger.info { "Sent request with UUID: $uniqueId (yay!)" }
    }
}