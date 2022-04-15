package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.getStringDataContext

class PostManiaTitleCardRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/mania-title-card"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val stringsContext = call.getStringDataContext()
                val line1 = stringsContext.retrieveString(0)
                val line2 = stringsContext.retrieveStringOrNull(1)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.maniaTitleCardGenerator.generate(
                        line1,
                        line2
                    )
                }

                call.respondBytes(result.toByteArray(ImageFormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}