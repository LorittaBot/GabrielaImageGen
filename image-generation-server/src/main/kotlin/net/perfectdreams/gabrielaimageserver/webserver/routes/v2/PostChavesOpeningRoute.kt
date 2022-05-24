package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.ChavesOpeningRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage

class PostChavesOpeningRoute(m: GabrielaImageGen) : SimpleAPIv2Route<ChavesOpeningRequest>(
    "/videos/chaves-opening",
    m,
    ContentType.Video.MP4
) {
    override val deserializationBlock: (String) -> ChavesOpeningRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: ChavesOpeningRequest): ByteArray {
        val (chiquinha, girafales, bruxa, quico, florinda, madruga, barriga, chaves, text) = data

        val image1 = chiquinha.retrieveImage(m.connectionManager)
        val image2 = girafales.retrieveImage(m.connectionManager)
        val image3 = bruxa.retrieveImage(m.connectionManager)
        val image4 = quico.retrieveImage(m.connectionManager)
        val image5 = florinda.retrieveImage(m.connectionManager)
        val image6 = madruga.retrieveImage(m.connectionManager)
        val image7 = barriga.retrieveImage(m.connectionManager)
        val image8 = chaves.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(m.generators.chavesOpeningGenerator) {
            generate(
                image1,
                image2,
                image3,
                image4,
                image5,
                image6,
                image7,
                image8,
                text
            )
        }
    }
}