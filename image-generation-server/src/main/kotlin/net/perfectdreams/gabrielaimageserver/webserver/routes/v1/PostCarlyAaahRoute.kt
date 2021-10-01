package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostCarlyAaahRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.carlyAaahGenerator,
    ContentType.Video.MP4,
    "/videos/carly-aaah"
)