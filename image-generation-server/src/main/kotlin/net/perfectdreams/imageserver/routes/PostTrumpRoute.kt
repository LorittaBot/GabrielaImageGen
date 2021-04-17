package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.getImageDataContext
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData

class PostTrumpRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/images/trump"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val imageData = call.getImageDataContext()
                val sourceImage1 = imageData.retrieveImage(0)
                val sourceImage2 = imageData.retrieveImage(1)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.trumpGenerator.generate(sourceImage1, sourceImage2)
                }

                call.respondBytes(result, ContentType.Image.GIF)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}