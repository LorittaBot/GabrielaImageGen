package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.getImageDataContext

class PostTrumpRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/trump"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val imageData = call.getImageDataContext(m.connectionManager)
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