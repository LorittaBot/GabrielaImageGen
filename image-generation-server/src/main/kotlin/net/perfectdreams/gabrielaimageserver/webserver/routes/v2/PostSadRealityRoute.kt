package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.SadRealityRequest
import net.perfectdreams.gabrielaimageserver.generators.SadRealityGenerator
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
        val (user1, user2, user3, user4, user5, user6) = data

        val user1BufferedImage = SadRealityGenerator.SadRealityUserWithBufferedImage(
            user1.text,
            user1.name,
            user1.image.retrieveImage(m.connectionManager)
        )

        val user2BufferedImage = SadRealityGenerator.SadRealityUserWithBufferedImage(
            user2.text,
            user2.name,
            user2.image.retrieveImage(m.connectionManager)
        )

        val user3BufferedImage = SadRealityGenerator.SadRealityUserWithBufferedImage(
            user3.text,
            user3.name,
            user3.image.retrieveImage(m.connectionManager)
        )

        val user4BufferedImage = SadRealityGenerator.SadRealityUserWithBufferedImage(
            user4.text,
            user4.name,
            user4.image.retrieveImage(m.connectionManager)
        )

        val user5BufferedImage = SadRealityGenerator.SadRealityUserWithBufferedImage(
            user5.text,
            user5.name,
            user5.image.retrieveImage(m.connectionManager)
        )

        val user6BufferedImage = SadRealityGenerator.SadRealityUserWithBufferedImage(
            user6.text,
            user6.name,
            user6.image.retrieveImage(m.connectionManager)
        )

        return measureGeneratorLatency(m.generators.sadRealityGenerator) {
            generateImage(user1BufferedImage, user2BufferedImage, user3BufferedImage, user4BufferedImage, user5BufferedImage, user6BufferedImage)
        }
    }
}