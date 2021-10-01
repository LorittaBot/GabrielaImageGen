package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.MemeMakerRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import java.awt.image.BufferedImage

class PostMemeMakerRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<MemeMakerRequest>(
    "/images/meme-maker",
    m
) {
    override val deserializationBlock: (String) -> MemeMakerRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: MemeMakerRequest): BufferedImage {
        val (imageData, line1, line2) = data
        val image = imageData.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(m.generators.memeMakerGenerator) {
            generate(
                image,
                line1,
                line2
            )
        }
    }
}