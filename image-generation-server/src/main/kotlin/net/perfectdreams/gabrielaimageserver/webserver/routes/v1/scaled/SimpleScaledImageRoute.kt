package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.scaled

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.generators.BasicScaledImageGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostSingleSourceImageGeneratorRoute

abstract class SimpleScaledImageRoute(m: GabrielaImageGen, generator: BasicScaledImageGenerator, path: String) : PostSingleSourceImageGeneratorRoute(
    m,
    generator,
    ContentType.Image.PNG,
    "/images/$path"
)