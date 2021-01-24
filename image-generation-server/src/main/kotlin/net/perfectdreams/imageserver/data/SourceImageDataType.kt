package net.perfectdreams.imageserver.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SourceImageDataType {
    @SerialName("url")
    URL,
    @SerialName("base64")
    BASE64
}