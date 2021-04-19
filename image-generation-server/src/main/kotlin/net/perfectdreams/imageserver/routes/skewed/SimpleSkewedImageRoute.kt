package net.perfectdreams.imageserver.routes.skewed

import io.ktor.http.*
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.PostSingleSourceImageGeneratorRoute

abstract class SimpleSkewedImageRoute(m: GabrielaImageGen, generator: BasicSkewedImageGenerator, path: String) : PostSingleSourceImageGeneratorRoute(
    m,
    generator,
    ContentType.Image.PNG,
    "/images/$path"
)