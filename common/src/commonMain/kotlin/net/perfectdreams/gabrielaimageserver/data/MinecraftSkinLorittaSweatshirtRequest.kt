package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class MinecraftSkinLorittaSweatshirtRequest(
    val image: SourceImageData,
    val sweatshirtStyle: SweatshirtStyle
) {
    enum class SweatshirtStyle {
        LIGHT,
        DARK,
        MIX_WAVY,
        MIX_WAVY_WITH_STITCHES,
        MIX_VERTICAL,
        MIX_VERTICAL_WITH_STITCHES
    }
}