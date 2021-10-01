package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.TerminatorAnimeRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import java.awt.image.BufferedImage

class PostTerminatorAnimeRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<TerminatorAnimeRequest>(
    "/images/terminator-anime",
    m
) {
    override val deserializationBlock: (String) -> TerminatorAnimeRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: TerminatorAnimeRequest): BufferedImage {
        val (terminatorText, nonoMorikuboText) = data

        return measureGeneratorLatency(m.generators.terminatorAnimeGenerator) {
            generate(
                terminatorText,
                nonoMorikuboText
            )
        }
    }
}