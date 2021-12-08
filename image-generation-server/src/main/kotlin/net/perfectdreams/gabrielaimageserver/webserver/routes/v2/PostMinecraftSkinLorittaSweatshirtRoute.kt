package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.MinecraftSkinLorittaSweatshirtRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleBufferedImageAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import java.awt.image.BufferedImage

class PostMinecraftSkinLorittaSweatshirtRoute(m: GabrielaImageGen) : SimpleBufferedImageAPIv2Route<MinecraftSkinLorittaSweatshirtRequest>(
    "/images/minecraft-skin-loritta-sweatshirt",
    m
) {
    override val deserializationBlock: (String) -> MinecraftSkinLorittaSweatshirtRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: MinecraftSkinLorittaSweatshirtRequest): BufferedImage {
        val (imageData, sweatshirtStyle) = data
        val image = imageData.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(m.generators.minecraftSkinLorittaSweatshirtGenerator) {
            generate(image, sweatshirtStyle)
        }
    }
}