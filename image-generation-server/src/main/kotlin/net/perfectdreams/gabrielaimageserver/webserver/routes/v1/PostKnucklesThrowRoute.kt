package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostKnucklesThrowRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.knucklesThrowGenerator,
    ContentType.Image.GIF,
    "/images/knuckles-throw"
)