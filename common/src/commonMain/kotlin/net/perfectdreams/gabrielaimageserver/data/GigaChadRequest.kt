package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class GigaChadRequest(
    val virginLine: String,
    val gigachadLine: String,
)