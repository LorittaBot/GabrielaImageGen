package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.data.SourceImagesContext
import net.perfectdreams.imageserver.data.SourceStringsContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor

class PostMemeMakerGeneratorRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
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
                    JVMImage(
                        m.generators.memeMakerGenerator.generate(
                            imageData.retrieveImage(0),
                            stringData.retrieveString(0).toUpperCase(),
                            stringData.retrieveStringOrNull(1)?.toUpperCase(),
                        )
                    )
                }

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}