package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.drake

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.BasicDrakeImageGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.VersionedAPIv1Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.getImageDataContext

abstract class SimpleDrakeImageRoute(val m: GabrielaImageGen, val generator: BasicDrakeImageGenerator, path: String) : VersionedAPIv1Route(
    "/images/$path"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val imagesContext = call.getImageDataContext(m.connectionManager)

                val source1 = imagesContext.retrieveImage(0)
                val source2 = imagesContext.retrieveImage(1)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(
                        source1,
                        source2
                    )
                }

                call.respondBytes(result, ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}