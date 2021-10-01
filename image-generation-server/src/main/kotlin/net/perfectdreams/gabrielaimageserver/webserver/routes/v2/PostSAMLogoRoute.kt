package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.SAMLogoRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import java.awt.image.BufferedImage
import java.util.*

class PostSAMLogoRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<SAMLogoRequest>(
    "/images/sam",
    m
) {
    override val deserializationBlock: (String) -> SAMLogoRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: SAMLogoRequest): BufferedImage {
        val (imageData, type) = data

        val image = imageData.retrieveImage(m.connectionManager)

        val generator = when (type) {
            SAMLogoRequest.LogoType.SAM_1 -> m.generators.samLogo1Generator
            SAMLogoRequest.LogoType.SAM_2 -> m.generators.samLogo2Generator
            SAMLogoRequest.LogoType.SAM_3 -> m.generators.samLogo3Generator
        }

        val random = SplittableRandom()
        // We want to avoid having the logo way too much on the sides
        val xPercentage = random.nextDouble(0.2, 0.8)
        val yPercentage = random.nextDouble(0.2, 0.8)

        return measureGeneratorLatency(generator) {
            generate(image, xPercentage, yPercentage)
        }
    }
}