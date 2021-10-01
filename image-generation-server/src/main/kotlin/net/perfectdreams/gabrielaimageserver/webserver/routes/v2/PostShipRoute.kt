package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.ShipRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import java.awt.image.BufferedImage

class PostShipRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<ShipRequest>(
    "/images/ship",
    m
) {
    override val deserializationBlock: (String) -> ShipRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: ShipRequest): BufferedImage {
        val (image1Data, image2Data, percentage) = data
        val image1 = image1Data.retrieveImage(m.connectionManager)
        val image2 = image2Data.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(m.generators.shipGenerator) {
            generate(image1, image2, percentage)
        }
    }
}