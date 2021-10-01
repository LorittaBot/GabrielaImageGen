package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.ManiaTitleCardRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import java.awt.image.BufferedImage

class PostManiaTitleCardRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<ManiaTitleCardRequest>(
    "/images/mania-title-card",
    m
) {
    override val deserializationBlock: (String) -> ManiaTitleCardRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: ManiaTitleCardRequest): BufferedImage {
        val (line1, line2) = data

        return measureGeneratorLatency(m.generators.maniaTitleCardGenerator) {
            generate(
                line1,
                line2
            )
        }
    }
}