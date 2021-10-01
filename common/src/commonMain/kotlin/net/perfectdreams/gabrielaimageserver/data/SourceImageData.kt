package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
sealed class SourceImageData

@Serializable
data class URLImageData(val url: String) : SourceImageData()

@Serializable
data class Base64ImageData(val data: String) : SourceImageData()