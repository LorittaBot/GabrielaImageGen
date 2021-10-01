package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class PetPetRequest(
    val image: SourceImageData,
    val squish: Double = 0.875,
    val delayBetweenFrames: Int = 7
)