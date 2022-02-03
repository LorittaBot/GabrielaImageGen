package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class SadRealityRequest(
    val text1: String,
    val text2: String,
    val text3: String,
    val text4: String,
    val text5: String,
    val text6: String,
    val image1: SourceImageData,
    val image2: SourceImageData,
    val image3: SourceImageData,
    val image4: SourceImageData,
    val image5: SourceImageData,
    val image6: SourceImageData,
)