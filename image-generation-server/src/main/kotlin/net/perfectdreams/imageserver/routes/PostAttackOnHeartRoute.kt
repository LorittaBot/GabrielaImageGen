package net.perfectdreams.imageserver.routes

import io.ktor.http.*
import net.perfectdreams.imageserver.GabrielaImageGen

class PostAttackOnHeartRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.attackOnHeartGenerator,
    ContentType.Video.MP4,
    "/videos/attack-on-heart"
)