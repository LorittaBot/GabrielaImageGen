package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mu.KotlinLogging
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.data.SourceImagesContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor

class PostShipRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/images/ship"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val postResult = call.receiveText()

                val json = Json.encodeToJsonElement(postResult)
                    .jsonObject
                val percentage = json["percentage"]!!.jsonPrimitive.int

                val imagesContext = SourceImagesContext.from(m.connectionManager, postResult)
                val image1 = imagesContext.retrieveImage(0)
                val image2 = imagesContext.retrieveImage(1)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.shipGenerator.generate(
                        image1,
                        image2,
                        percentage
                    )
                }

                call.respondBytes(result.toByteArray(Image.FormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}