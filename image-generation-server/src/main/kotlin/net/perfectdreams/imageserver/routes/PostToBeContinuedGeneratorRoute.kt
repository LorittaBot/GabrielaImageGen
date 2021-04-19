package net.perfectdreams.imageserver.routes

import io.ktor.http.*
import net.perfectdreams.imageserver.GabrielaImageGen

class PostToBeContinuedGeneratorRoute(m: GabrielaImageGen) : PostSingleSourceImageGeneratorRoute(
    m,
    m.generators.toBeContinuedGenerator,
    ContentType.Image.PNG,
    "/images/to-be-continued"
)