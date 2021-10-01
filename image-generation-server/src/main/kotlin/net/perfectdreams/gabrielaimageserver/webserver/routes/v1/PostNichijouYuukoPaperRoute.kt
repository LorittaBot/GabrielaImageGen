package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen

class PostNichijouYuukoPaperRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.nichijouYuukoPaperGenerator,
    ContentType.Image.GIF,
    "/images/nichijou-yuuko-paper"
)