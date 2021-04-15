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
import java.util.*

class PostManiaTitleCardRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
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

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}