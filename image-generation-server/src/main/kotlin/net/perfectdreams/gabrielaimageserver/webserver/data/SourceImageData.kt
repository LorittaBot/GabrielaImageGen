package net.perfectdreams.gabrielaimageserver.webserver.data

import kotlinx.serialization.Serializable

@Serializable
class SourceImageData(val type: SourceImageDataType, val content: String)