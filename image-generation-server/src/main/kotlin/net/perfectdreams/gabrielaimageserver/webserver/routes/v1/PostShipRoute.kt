package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceImagesContext
import net.perfectdreams.gabrielaimageserver.webserver.utils.ImageUtils.toByteArray
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor

class PostShipRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/ship"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val postResult = call.receiveText()

                val json = Json.parseToJsonElement(postResult)
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

                call.respondBytes(result.toByteArray(ImageFormatType.PNG), ContentType.Image.PNG)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}