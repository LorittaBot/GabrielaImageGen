package net.perfectdreams.imageserver.routes.skewed

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.BaseRoute
import net.perfectdreams.imageserver.routes.getImageDataContext
import net.perfectdreams.imageserver.utils.Constants
import java.util.*

abstract class SimpleSkewedImageRoute(val m: GabrielaImageGen, val generator: BasicSkewedImageGenerator, path: String) : BaseRoute(
    "/api/${Constants.API_VERSION}/images/$path"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        val uniqueId = UUID.randomUUID()

        logger.info { "Received request with UUID: $uniqueId" }
        val imagesContext = call.getImageDataContext()

        val theRealImageOwO = imagesContext.retrieveImage(0)

        val result = withContext(m.coroutineDispatcher) {
            generator.generate(JVMImage(theRealImageOwO))
        }

        call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
        logger.info { "Sent request with UUID: $uniqueId (yay!)" }
    }
}