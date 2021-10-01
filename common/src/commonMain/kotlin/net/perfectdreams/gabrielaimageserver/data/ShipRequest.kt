package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class ShipRequest(
    val image1: SourceImageData,
    val image2: SourceImageData,
    val percentage: Int
)