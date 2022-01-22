package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.DrawnMaskWordRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import java.awt.image.BufferedImage

class PostDrawnMaskWordRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<DrawnMaskWordRequest>(
    "/images/drawn-mask-word",
    m
) {
    override val deserializationBlock: (String) -> DrawnMaskWordRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: DrawnMaskWordRequest): BufferedImage {
        val (text) = data

        return measureGeneratorLatency(m.generators.drawnMaskWordGenerator) {
            generate(
                text
            )
        }
    }
}