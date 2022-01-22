package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.DrawnMaskAtendenteRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import java.awt.image.BufferedImage

class PostDrawnMaskAtendenteRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<DrawnMaskAtendenteRequest>(
    "/images/drawn-mask-atendente",
    m
) {
    override val deserializationBlock: (String) -> DrawnMaskAtendenteRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: DrawnMaskAtendenteRequest): BufferedImage {
        val (text) = data

        return measureGeneratorLatency(m.generators.drawnMaskAtendenteGenerator) {
            generate(
                text
            )
        }
    }
}