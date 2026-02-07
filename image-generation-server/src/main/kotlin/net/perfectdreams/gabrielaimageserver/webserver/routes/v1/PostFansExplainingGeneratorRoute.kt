package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.getStringDataContext

class PostFansExplainingGeneratorRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/videos/fans-explaining"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val stringContext = call.getStringDataContext()

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.fansExplainingGenerator.generate(
                        stringContext.retrieveString(0).uppercase(),
                        stringContext.retrieveString(1).uppercase(),
                        stringContext.retrieveString(2).uppercase(),
                        stringContext.retrieveString(3).uppercase(),
                        stringContext.retrieveString(4).uppercase(),
                        stringContext.retrieveString(5).uppercase(),
                        stringContext.retrieveString(6).uppercase(),
                        stringContext.retrieveString(7).uppercase(),
                        stringContext.retrieveString(8).uppercase(),
                        stringContext.retrieveString(9).uppercase()
                    )
                }

                call.respondBytes(result, ContentType.Video.MP4)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}