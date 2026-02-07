package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.getStringDataContext

class PostTerminatorAnimeRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/terminator-anime"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val stringData = call.getStringDataContext()
                val input1 = stringData.retrieveString(0)
                val input2 = stringData.retrieveString(1)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.terminatorAnimeGenerator.generate(input1, input2)
                }

                call.respondBytes(result.toByteArray(ImageFormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}