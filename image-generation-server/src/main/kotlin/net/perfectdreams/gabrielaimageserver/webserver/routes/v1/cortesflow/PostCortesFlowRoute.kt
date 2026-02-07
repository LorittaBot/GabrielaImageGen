package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.cortesflow

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.CortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.VersionedAPIv1Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.retrieveStringFromStringData

open class PostCortesFlowRoute(val m: GabrielaImageGen, val generator: CortesFlowGenerator, path: String) : VersionedAPIv1Route(
        "/images/cortes-flow/$path"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val sourceString = call.retrieveStringFromStringData(0)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(sourceString)
                }

                call.respondBytes(result.toByteArray(ImageFormatType.JPEG), ContentType.Image.JPEG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}