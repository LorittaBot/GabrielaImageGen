package net.perfectdreams.imagegen.utils.gifs.palettecreators

import java.awt.Color
import kotlin.math.sqrt

/**
 * Naive [PaletteCreator] implementation by using the most common pixels in the palette. Faster than [NeuQuantPaletteCreator], but isn't very good.
 */
class NaivePaletteCreator : PaletteCreator {
    companion object {
        // https://stackoverflow.com/a/9085524/7271796
        fun colorDistance(r1: Int, g1: Int, b1: Int, r2: Int, g2: Int, b2: Int): Double {
            // Quick Optimization: If both colors are identical, fast return 0.0 to avoid expensive calculations
            if (r1 == r2 && g1 == g2 && b1 == b2)
                return 0.0

            val rMean = (r1.toLong() + r2.toLong()) / 2
            val rDist = r1.toLong() - r2.toLong()
            val gDist = g1.toLong() - g2.toLong()
            val bDist = b1.toLong() - b2.toLong()
            return sqrt(((512 + rMean) * rDist * rDist shr 8) + 4 * gDist * gDist + ((767 - rMean) * bDist * bDist shr 8).toDouble())
        }
    }

    override fun createPaletteFromPixels(
        pixels: ByteArray,
        indexedPixels: ByteArray,
        transparent: Color?,
        hasTransparentPixels: Boolean
    ): PaletteCreatorResult {
        val len = pixels.size
        val nPix = len / 3

        // Naive palette implementation, bad quality... but it does work!
        // What we will do is reduce our current BGR palette (the "pixels" array), storing it based on how many times the palette is found
        val rgbValues = mutableMapOf<Color, Int>()
        var k1 = 0
        for (i in 0 until nPix) {
            val b = pixels[k1++].toInt() and 0xff
            val g = pixels[k1++].toInt() and 0xff
            val r = pixels[k1++].toInt() and 0xff
            val color = Color(r, g, b)
            rgbValues[color] = rgbValues.getOrPut(color) { 0 }
        }

        val colorsToBeWritten = rgbValues.entries.sortedByDescending { it.value }
            .take(256)
            .map { it.key }
            .toMutableList()

        // If there's transparency in this GIF, and the transparent color is not in the GIF, then remove the last less known color and add it!
        if (transparent != null && transparent !in colorsToBeWritten) {
            if (colorsToBeWritten.size >= 256) // We only need to remove it, if it wouldn't fit in our palette
                colorsToBeWritten.removeLast()
            colorsToBeWritten.add(transparent)
        }

        val colorsToBeWrittenAndFlattened = colorsToBeWritten
            .map {
                listOf(
                    it.red.toByte(),
                    it.green.toByte(),
                    it.blue.toByte()
                )
            }
            .flatten()

        val colorTab = colorsToBeWrittenAndFlattened.toByteArray()

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