package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostInvertColorsGeneratorRoute(m: GabrielaImageGen) : PostSingleSourceImageGeneratorRoute(
    m,
    m.generators.invertColorsGenerator,
    ContentType.Image.PNG,
    "/images/invert-colors"
)