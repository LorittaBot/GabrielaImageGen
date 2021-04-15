package net.perfectdreams.imageserver.routes.cortesflow

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.cortesflow.CortesFlowGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.VersionedAPIRoute
import net.perfectdreams.imageserver.routes.drake.SimpleDrakeImageRoute
import net.perfectdreams.imageserver.utils.extensions.getStringDataContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.retrieveStringFromStringData
import java.util.*

open class PostCortesFlowRoute(val m: GabrielaImageGen, val generator: CortesFlowGenerator, path: String) : VersionedAPIRoute(
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

                call.respondBytes(JVMImage(result).toByteArray(Image.FormatType.JPEG), ContentType.Image.JPEG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}