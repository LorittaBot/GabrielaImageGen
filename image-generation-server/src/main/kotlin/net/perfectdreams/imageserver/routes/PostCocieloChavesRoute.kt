package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.getImageDataContext

class PostCocieloChavesRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
        "/videos/cocielo-chaves"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val highQualitySemaphore = Semaphore(2)
        private val lowQualitySemaphore = Semaphore(4)
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val imagesContext = call.getImageDataContext()

                val sourceImage1 = imagesContext.retrieveImage(0)
                val sourceImage2 = imagesContext.retrieveImage(1)
                val sourceImage3 = imagesContext.retrieveImage(2)
                val sourceImage4 = imagesContext.retrieveImage(3)
                val sourceImage5 = imagesContext.retrieveImage(4)

                val result = withContext(m.coroutineDispatcher) {
                    // This is kinda oof
                    //
                    // The video takes around ~32s if there is 4 parallel edits being executed
                    // So we are going to limit the video generation to only 4 in parallel
                    //
                    // We are going to limit the video generation into two: A high quality (30fps) and a low quality (15fps)
                    val useDecreasedFrames = highQualitySemaphore.availablePermits == 0
                    logger.info { "Request with UUID: $it; Use decreased frames? $useDecreasedFrames; High Quality Permits: ${highQualitySemaphore.availablePermits}; Low Quality Permits: ${lowQualitySemaphore.availablePermits}" }
                    val semaphoreToBeUsed = if (useDecreasedFrames)
                        lowQualitySemaphore
                    else
                        highQualitySemaphore

                    semaphoreToBeUsed.withPermit {
                        m.generators.cocieloChavesGenerator.generate(
                            sourceImage1,
                            sourceImage2,
                            sourceImage3,
                            sourceImage4,
                            sourceImage5,
                            useDecreasedFrames
                        )
                    }
                }

                call.respondBytes(result, ContentType.Video.MP4)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}