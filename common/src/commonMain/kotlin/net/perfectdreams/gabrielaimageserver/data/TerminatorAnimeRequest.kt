package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class TerminatorAnimeRequest(
    val terminatorText: String,
    val nonoMorikuboText: String
)