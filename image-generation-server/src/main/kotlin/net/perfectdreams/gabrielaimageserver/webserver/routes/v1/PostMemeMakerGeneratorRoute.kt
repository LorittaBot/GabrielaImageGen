package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceImagesContext
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceStringsContext
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor

class PostMemeMakerGeneratorRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/meme-maker"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                // We can't use directly because we need to read the payload twice (for the strings and for the image)
                // and that causes issues!
                //
                // So we read the text and then parse it afterwards
                val text = call.receiveText()
                val stringData = SourceStringsContext.from(text)
                val imageData = SourceImagesContext.from(m.connectionManager, text)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.memeMakerGenerator.generate(
                        imageData.retrieveImage(0),
                        stringData.retrieveString(0).toUpperCase(),
                        stringData.retrieveStringOrNull(1)?.toUpperCase(),
                    )
                }

                call.respondBytes(result.toByteArray(ImageFormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}