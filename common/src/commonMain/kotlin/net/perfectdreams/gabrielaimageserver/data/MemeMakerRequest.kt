package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class MemeMakerRequest(
    val image: SourceImageData,
    val line1: String,
    val line2: String? = null
)