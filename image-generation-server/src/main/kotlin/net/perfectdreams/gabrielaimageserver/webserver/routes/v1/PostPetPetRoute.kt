package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.generators.PetPetGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceImagesContext
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor

class PostPetPetRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/pet-pet"
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
                val squish = json["squish"]?.jsonPrimitive?.double ?: 0.875
                val delayBetweenFrames = json["speed"]?.jsonPrimitive?.int ?: 7

                val imagesContext = SourceImagesContext.from(m.connectionManager, postResult)
                val image1 = imagesContext.retrieveImage(0)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.petPetGenerator.generate(
                        image1,
                        PetPetGenerator.PetPetOptions(
                            squish,
                            delayBetweenFrames
                        )
                    )
                }

                call.respondBytes(
                    result,
                    ContentType.Image.GIF
                )
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}