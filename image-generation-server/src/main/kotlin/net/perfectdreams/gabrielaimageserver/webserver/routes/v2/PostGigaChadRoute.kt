package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.GigaChadRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleAPIv2Route

class PostGigaChadRoute(m: GabrielaImageGen) : SimpleAPIv2Route<GigaChadRequest>(
    "/videos/gigachad",
    m,
    ContentType.Video.MP4
) {
    override val deserializationBlock: (String) -> GigaChadRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: GigaChadRequest): ByteArray {
        val (section1Line1, section1Line2) = data

        return measureGeneratorLatency(m.generators.gigachadGenerator) {
            generate(
                section1Line1,
                section1Line2
            )
        }
    }
}