package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData

class PostPetPetRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
    "/images/pet-pet"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val sourceImage = call.retrieveImageFromImageData(0)

                val result = withContext(m.coroutineDispatcher) {
                    m.generators.petPetGenerator.generate(sourceImage)
                }

                call.respondBytes(result, ContentType.Image.GIF)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}