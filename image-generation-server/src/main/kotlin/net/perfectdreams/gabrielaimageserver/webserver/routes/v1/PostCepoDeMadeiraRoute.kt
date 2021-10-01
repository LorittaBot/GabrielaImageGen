package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostCepoDeMadeiraRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.cepoDeMadeiraGenerator,
    ContentType.Image.GIF,
    "/images/cepo-de-madeira"
)