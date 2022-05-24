package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.ColorInfoRequest
import net.perfectdreams.gabrielaimageserver.data.SadRealityRequest
import net.perfectdreams.gabrielaimageserver.generators.SadRealityGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import java.awt.Color
import java.awt.image.BufferedImage

class PostColorInfoRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<ColorInfoRequest>(
    "/images/color-info",
    m
) {
    override val deserializationBlock: (String) -> ColorInfoRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: ColorInfoRequest): BufferedImage {
        val (
            color,
            triadColor1,
            triadColor2,
            analogousColor1,
            analogousColor2,
            complementaryColor,
            shades,
            tints,
            triadic,
            analogous,
            complementary
        ) = data

        return measureGeneratorLatency(m.generators.colorInfoGenerator) {
            generate(
                Color(color.red, color.green, color.blue),
                Color(triadColor1.red, triadColor1.green, triadColor1.blue),
                Color(triadColor2.red, triadColor2.green, triadColor2.blue),
                Color(analogousColor1.red, analogousColor1.green, analogousColor1.blue),
                Color(analogousColor2.red, analogousColor2.green, analogousColor2.blue),
                Color(complementaryColor.red, complementaryColor.green, complementaryColor.blue),
                shades,
                tints,
                triadic,
                analogous,
                complementary
            )
        }
    }
}