package net.perfectdreams.imageserver.routes.scaled

import io.ktor.http.*
import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.PostSingleSourceImageGeneratorRoute

abstract class SimpleScaledImageRoute(m: GabrielaImageGen, generator: BasicScaledImageGenerator, path: String) : PostSingleSourceImageGeneratorRoute(
    m,
    generator,
    ContentType.Image.PNG,
    "/images/$path"
)