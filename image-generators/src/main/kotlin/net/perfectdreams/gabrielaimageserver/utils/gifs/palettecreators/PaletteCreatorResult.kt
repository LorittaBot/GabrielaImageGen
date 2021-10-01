package net.perfectdreams.gabrielaimageserver.utils.gifs.palettecreators

data class PaletteCreatorResult(
    val colorTab: ByteArray,
    val colorDepth: Int,
    val palSize: Int,
    val transIndex: Int
)