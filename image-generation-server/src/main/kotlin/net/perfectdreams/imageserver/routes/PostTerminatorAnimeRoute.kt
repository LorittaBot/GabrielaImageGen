package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.getStringDataContext
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData

class PostTerminatorAnimeRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
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

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}