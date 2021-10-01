package net.perfectdreams.gabrielaimageserver.utils.gifs.palettecreators

import java.awt.Color

interface PaletteCreator {
    fun createPaletteFromPixels(pixels: ByteArray, indexedPixels: ByteArray, transparent: Color?, hasTransparentPixels: Boolean): PaletteCreatorResult
}