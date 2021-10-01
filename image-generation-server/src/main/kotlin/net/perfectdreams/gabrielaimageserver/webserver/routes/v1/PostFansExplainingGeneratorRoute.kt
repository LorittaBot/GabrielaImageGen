package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
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
                        stringContext.retrieveString(0).toUpperCase(),
                        stringContext.retrieveString(1).toUpperCase(),
                        stringContext.retrieveString(2).toUpperCase(),
                        stringContext.retrieveString(3).toUpperCase(),
                        stringContext.retrieveString(4).toUpperCase(),
                        stringContext.retrieveString(5).toUpperCase(),
                        stringContext.retrieveString(6).toUpperCase(),
                        stringContext.retrieveString(7).toUpperCase(),
                        stringContext.retrieveString(8).toUpperCase(),
                        stringContext.retrieveString(9).toUpperCase()
                    )
                }

                call.respondBytes(result, ContentType.Video.MP4)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}