package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.getImageDataContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import java.util.*

class PostAttackOnHeartRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/videos/attack-on-heart"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            val uniqueId = UUID.randomUUID()

            logger.info { "Received request with UUID: $uniqueId" }
            val imagesContext = call.getImageDataContext()

            val sourceImage = imagesContext.retrieveImage(0)

            val result = withContext(m.coroutineDispatcher) {
                m.generators.ATTACK_ON_HEART_GENERATOR.generate(sourceImage)
            }

            call.respondBytes(result, ContentType.Video.MP4)
            logger.info { "Sent request with UUID: $uniqueId (yay!)" }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}