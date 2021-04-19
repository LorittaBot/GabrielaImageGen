package net.perfectdreams.imageserver.routes

import io.ktor.http.*
import net.perfectdreams.imageserver.GabrielaImageGen

class PostPetPetRoute(m: GabrielaImageGen) : PostSingleSourceImageToByteArrayGeneratorRoute(
    m,
    m.generators.petPetGenerator,
    ContentType.Image.GIF,
    "/images/pet-pet"
)