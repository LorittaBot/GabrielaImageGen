package net.perfectdreams.imageserver.routes

import io.ktor.http.*
import net.perfectdreams.imageserver.GabrielaImageGen

class PostCarlyAaahRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.carlyAaahGenerator,
    ContentType.Video.MP4,
    "/videos/carly-aaah"
)