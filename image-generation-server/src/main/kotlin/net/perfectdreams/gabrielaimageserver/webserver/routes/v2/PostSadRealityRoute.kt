package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.SadRealityRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import java.awt.image.BufferedImage

class PostSadRealityRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<SadRealityRequest>(
    "/images/sad-reality",
    m
) {
    override val deserializationBlock: (String) -> SadRealityRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: SadRealityRequest): BufferedImage {
        val (text1, text2, text3, text4, text5, text6, image1Data, image2Data, image3Data, image4Data, image5Data, image6Data) = data
        val image1 = image1Data.retrieveImage(m.connectionManager)
        val image2 = image2Data.retrieveImage(m.connectionManager)
        val image3 = image3Data.retrieveImage(m.connectionManager)
        val image4 = image4Data.retrieveImage(m.connectionManager)
        val image5 = image5Data.retrieveImage(m.connectionManager)
        val image6 = image6Data.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(m.generators.sadRealityGenerator) {
            generateImage(text1, text2, text3, text4, text5, text6, image1, image2, image3, image4, image5, image6)
        }
    }
}