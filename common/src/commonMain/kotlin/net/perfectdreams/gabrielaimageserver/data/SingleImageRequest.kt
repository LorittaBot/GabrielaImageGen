package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class SingleImageRequest(
    val image: SourceImageData
)