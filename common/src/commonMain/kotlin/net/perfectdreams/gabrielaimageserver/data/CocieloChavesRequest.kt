package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class CocieloChavesRequest(
    val image1: SourceImageData,
    val image2: SourceImageData,
    val image3: SourceImageData,
    val image4: SourceImageData,
    val image5: SourceImageData,
)