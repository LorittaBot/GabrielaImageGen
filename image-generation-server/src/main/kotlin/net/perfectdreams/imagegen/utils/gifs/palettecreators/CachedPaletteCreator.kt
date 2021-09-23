package net.perfectdreams.imagegen.utils.gifs.palettecreators

import java.awt.Color

/**
 * Uses a pre-created palette for the image, useful if you don't want to recalculate the palette on each frame.
 */
class CachedPaletteCreator(private val colorTab: ByteArray) : PaletteCreator {
    override fun createPaletteFromPixels(
        pixels: ByteArray,
        indexedPixels: ByteArray,
        transparent: Color?,
        hasTransparentPixels: Boolean
    ): PaletteCreatorResult {
        val transparentIndex = RGBPaletteToGIFPaletteConverter.convert(
            pixels,
            colorTab,
            indexedPixels,
            transparent
        )

        // Get closest match to transparent color if specified and if the current frame has transparent pixels
        // We check if they are present to avoid issues if the frame only has a solid color without any transparency
        val transIndex = if (hasTransparentPixels) {
            transparentIndex
        } else {
            -1 // reset transparent index to avoid issues (magic value)
        }

        return PaletteCreatorResult(
            colorTab,
            8,
            7,
            transIndex
        )
    }
}