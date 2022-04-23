package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class ColorInfoRequest(
    val color: Color,
    val triadColor1: Color,
    val triadColor2: Color,
    val analogousColor1: Color,
    val analogousColor2: Color,
    val complementaryColor: Color,
    val shades: String,
    val tints: String,
    val triadic: String,
    val analogous: String,
    val complementary: String
) {
    @Serializable
    data class Color(
        val red: Int,
        val green: Int,
        val blue: Int
    )
}