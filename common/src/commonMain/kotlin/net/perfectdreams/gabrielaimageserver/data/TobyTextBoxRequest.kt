package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class TobyTextBoxRequest(
    val text: String,
    val type: TextBoxType,
    val portrait: String? = null,
    val image: SourceImageData? = null,
    val colorPortraitType: ColorPortraitType? = null,
) {
    enum class TextBoxType {
        ORIGINAL,
        DARK_WORLD
    }

    enum class ColorPortraitType {
        COLORED,
        BLACK_AND_WHITE,
        SHADES_OF_GRAY
    }
}