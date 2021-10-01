package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.retrieveImageFromImageData
import java.util.*

// Tio SAM, socorro!
// https://youtu.be/jse4Lc9j28c
class PostSAMLogoRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
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
                    generator.generate(sourceImage, xPercentage, yPercentage)
                }

                call.respondBytes(result.toByteArray(ImageFormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}