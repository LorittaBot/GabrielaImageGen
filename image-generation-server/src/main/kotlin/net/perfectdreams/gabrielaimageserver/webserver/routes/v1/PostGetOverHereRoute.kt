package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostGetOverHereRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.getOverHereGenerator,
    ContentType.Image.GIF,
    "/images/get-over-here"
)