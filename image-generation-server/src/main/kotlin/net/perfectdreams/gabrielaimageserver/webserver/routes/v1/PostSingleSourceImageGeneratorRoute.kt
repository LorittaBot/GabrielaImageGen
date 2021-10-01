package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.SingleSourceImageToImageGenerator
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.retrieveImageFromImageData

open class PostSingleSourceImageGeneratorRoute(
    val m: GabrielaImageGen,
    val generator: SingleSourceImageToImageGenerator,
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
                    generator.generateImage(sourceImage)
                }

                call.respondBytes(result.toByteArray(ImageFormatType.PNG), contentType)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}