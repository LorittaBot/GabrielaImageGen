package net.perfectdreams.imageserver.routes

import io.ktor.http.*
import net.perfectdreams.imageserver.GabrielaImageGen

class PostInvertColorsGeneratorRoute(m: GabrielaImageGen) : PostSingleSourceImageGeneratorRoute(
    m,
    m.generators.invertColorsGenerator,
    ContentType.Image.PNG,
    "/images/invert-colors"
)