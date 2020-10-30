package net.perfectdreams.imageserver.data

import kotlinx.serialization.Serializable

@Serializable
class SourceImageData(val type: SourceImageDataType, val content: String)