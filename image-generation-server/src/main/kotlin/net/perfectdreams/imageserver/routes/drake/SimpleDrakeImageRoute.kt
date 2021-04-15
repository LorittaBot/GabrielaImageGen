package net.perfectdreams.imageserver.routes.drake

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.BasicDrakeImageGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.VersionedAPIRoute
import net.perfectdreams.imageserver.routes.scaled.SimpleScaledImageRoute
import net.perfectdreams.imageserver.utils.extensions.getImageDataContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import java.util.*

abstract class SimpleDrakeImageRoute(val m: GabrielaImageGen, val generator: BasicDrakeImageGenerator, path: String) : VersionedAPIRoute(
    "/images/$path"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val imagesContext = call.getImageDataContext()

                val source1 = imagesContext.retrieveImage(0)
                val source2 = imagesContext.retrieveImage(1)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(
                        JVMImage(source1),
                        JVMImage(source2)
                    )
                }

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}