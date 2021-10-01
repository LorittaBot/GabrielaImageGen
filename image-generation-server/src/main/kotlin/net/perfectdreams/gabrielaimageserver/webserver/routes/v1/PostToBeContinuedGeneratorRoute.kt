package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostToBeContinuedGeneratorRoute(m: GabrielaImageGen) : PostSingleSourceImageGeneratorRoute(
    m,
    m.generators.toBeContinuedGenerator,
    ContentType.Image.PNG,
    "/images/to-be-continued"
)