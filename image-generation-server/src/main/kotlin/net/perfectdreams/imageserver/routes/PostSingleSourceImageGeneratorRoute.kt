package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.SingleSourceImageGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData

open class PostSingleSourceImageGeneratorRoute(
    val m: GabrielaImageGen,
    val generator: SingleSourceImageGenerator,
    val contentType: ContentType,
    path: String
) : VersionedAPIRoute(
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
                    generator.generate(JVMImage(sourceImage))
                }

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), contentType)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}