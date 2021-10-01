package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.PetPetRequest
import net.perfectdreams.gabrielaimageserver.generators.PetPetGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage

class PostPetPetRoute(m: GabrielaImageGen) : SimpleAPIv2Route<PetPetRequest>(
    "/images/pet-pet",
    m,
    ContentType.Image.GIF
) {
    override val deserializationBlock: (String) -> PetPetRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: PetPetRequest): ByteArray {
        val (imageData, squish, delayBetweenFrames) = data
        val image1 = imageData.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(m.generators.petPetGenerator) {
            generate(
                image1,
                PetPetGenerator.PetPetOptions(
                    squish,
                    delayBetweenFrames
                )
            )
        }
    }
}