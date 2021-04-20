package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData
import java.util.*

// Tio SAM, socorro!
// https://youtu.be/jse4Lc9j28c
class PostSAMLogoRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/images/sam/{type}"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            val type = call.parameters["type"]

            withRequest(logger) {
                val sourceImage = call.retrieveImageFromImageData(m.connectionManager, 0)

                val generator = when (type) {
                    "1" -> m.generators.samLogo1Generator
                    "2" -> m.generators.samLogo2Generator
                    "3" -> m.generators.samLogo3Generator
                    else -> throw IllegalArgumentException("I don't know how to handle type $type SAM logo!")
                }

                val random = SplittableRandom()
                // We want to avoid having the logo way too much on the sides
                val xPercentage = random.nextDouble(0.2, 0.8)
                val yPercentage = random.nextDouble(0.2, 0.8)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(JVMImage(sourceImage), xPercentage, yPercentage)
                }

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}