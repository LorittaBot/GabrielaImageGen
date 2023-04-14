package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.SingleSourceImageToByteArrayGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.retrieveImageFromImageData

open class PostSingleSourceImageToByteArrayGeneratorRoute(
    val m: GabrielaImageGen,
    val generator: SingleSourceImageToByteArrayGenerator,
    val contentType: ContentType,
    path: String
) : VersionedAPIv1Route(
    path
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val sourceImage = call.retrieveImageFromImageData(m.connectionManager, 0)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(sourceImage)
                }

                call.respondBytes(result, contentType)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}