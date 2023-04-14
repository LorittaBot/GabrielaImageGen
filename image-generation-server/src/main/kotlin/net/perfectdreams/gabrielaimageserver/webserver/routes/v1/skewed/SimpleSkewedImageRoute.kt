package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.skewed

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostSingleSourceImageGeneratorRoute

abstract class SimpleSkewedImageRoute(m: GabrielaImageGen, generator: BasicSkewedImageGenerator, path: String) : PostSingleSourceImageGeneratorRoute(
    m,
    generator,
    ContentType.Image.PNG,
    "/images/$path"
)