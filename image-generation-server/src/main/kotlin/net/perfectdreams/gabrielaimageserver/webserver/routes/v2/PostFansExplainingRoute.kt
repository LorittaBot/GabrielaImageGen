package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.FansExplainingRequest
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleAPIv2Route

class PostFansExplainingRoute(m: GabrielaImageGen) : SimpleAPIv2Route<FansExplainingRequest>(
    "/videos/fans-explaining",
    m,
    ContentType.Video.MP4
) {
    override val deserializationBlock: (String) -> FansExplainingRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: FansExplainingRequest): ByteArray {
        val (section1Line1, section1Line2, section2Line1, section2Line2, section3Line1, section3Line2, section4Line1, section4Line2, section5Line1, section5Line2) = data

        return measureGeneratorLatency(m.generators.fansExplainingGenerator) {
            generate(
                section1Line1,
                section1Line2,
                section2Line1,
                section2Line2,
                section3Line1,
                section3Line2,
                section4Line1,
                section4Line2,
                section5Line1,
                section5Line2
            )
        }
    }
}