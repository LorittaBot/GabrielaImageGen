package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class DrawnMaskWordRequest(
    val text: String
)