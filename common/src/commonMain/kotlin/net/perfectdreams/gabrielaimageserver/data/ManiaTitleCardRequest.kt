package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class ManiaTitleCardRequest(
    val line1: String,
    val line2: String?
)