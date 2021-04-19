package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.SingleSourceBufferedImageToByteArrayGenerator
import net.perfectdreams.imagegen.generators.SingleSourceImageToByteArrayGenerator
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData

open class PostSingleSourceImageToByteArrayGeneratorRoute(
    val m: GabrielaImageGen,
    val generator: SingleSourceImageToByteArrayGenerator,
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
                val sourceImage = call.retrieveImageFromImageData(0)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(JVMImage(sourceImage))
                }

                call.respondBytes(result, contentType)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}