package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import io.ktor.http.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.data.CocieloChavesRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage

class PostCocieloChavesRoute(m: GabrielaImageGen) : SimpleAPIv2Route<CocieloChavesRequest>(
    "/videos/cocielo-chaves",
    m,
    ContentType.Video.MP4
) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val highQualitySemaphore = Semaphore(2)
        private val lowQualitySemaphore = Semaphore(4)
    }

    override val deserializationBlock: (String) -> CocieloChavesRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: CocieloChavesRequest): ByteArray {
        val (imageData1, imageData2, imageData3, imageData4, imageData5) = data

        val image1 = imageData1.retrieveImage(m.connectionManager)
        val image2 = imageData2.retrieveImage(m.connectionManager)
        val image3 = imageData3.retrieveImage(m.connectionManager)
        val image4 = imageData4.retrieveImage(m.connectionManager)
        val image5 = imageData5.retrieveImage(m.connectionManager)

        // This is kinda oof
        //
        // The video takes around ~32s if there is 4 parallel edits being executed
        // So we are going to limit the video generation to only 4 in parallel
        //
        // We are going to limit the video generation into two: A high quality (30fps) and a low quality (15fps)
        val useDecreasedFrames = highQualitySemaphore.availablePermits == 0
        logger.info { "Use decreased frames? $useDecreasedFrames; High Quality Permits: ${highQualitySemaphore.availablePermits}; Low Quality Permits: ${lowQualitySemaphore.availablePermits}" }
        val semaphoreToBeUsed = if (useDecreasedFrames)
            lowQualitySemaphore
        else
            highQualitySemaphore

        return measureGeneratorLatency(m.generators.cocieloChavesGenerator) {
            semaphoreToBeUsed.withPermit {
                m.generators.cocieloChavesGenerator.generate(
                    image1,
                    image2,
                    image3,
                    image4,
                    image5,
                    useDecreasedFrames
                )
            }
        }
    }
}