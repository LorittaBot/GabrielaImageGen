package net.perfectdreams.gabrielaimageserver.webserver.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SourceImageDataType {
    @SerialName("url")
    URL,
    @SerialName("base64")
    BASE64
}