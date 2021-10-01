package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class TwoImagesRequest(
    val image1: SourceImageData,
    val image2: SourceImageData
)