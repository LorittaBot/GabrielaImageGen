package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class ChavesOpeningRequest(
    val chiquinha: SourceImageData,
    val girafales: SourceImageData,
    val bruxa: SourceImageData,
    val quico: SourceImageData,
    val florinda: SourceImageData,
    val madruga: SourceImageData,
    val barriga: SourceImageData,
    val chaves: SourceImageData,
    val text: String
)