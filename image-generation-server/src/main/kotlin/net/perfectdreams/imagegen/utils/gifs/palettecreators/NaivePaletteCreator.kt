package net.perfectdreams.imagegen.utils.gifs.palettecreators

import java.awt.Color
import kotlin.math.sqrt

/**
 * Naive [PaletteCreator] implementation by using the most common pixels in the palette. Faster than [NeuQuantPaletteCreator], but isn't very good.
 */
class NaivePaletteCreator : PaletteCreator {
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
            val b = pixels!![k1++].toInt() and 0xff
            val g = pixels!![k1++].toInt() and 0xff
            val r = pixels!![k1++].toInt() and 0xff
            val color = Color(r, g, b)
            rgbValues[color] = rgbValues.getOrPut(color) { 0 }
        }

        val colorsToBeWritten = rgbValues.entries.sortedByDescending { it.value }
            .take(256)
            .map { it.key }
            .toMutableList()

        // If there's transparency in this GIF, and the transparent color is not in the GIF, then remove the last less known color and add it!
        if (transparent != null && transparent !in colorsToBeWritten) {
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

        var transparentIndex: Byte = -1
        var k = 0
        for (i in 0 until nPix) {
            val b = pixels[k++].toInt() and 0xff
            val g = pixels[k++].toInt() and 0xff
            val r = pixels[k++].toInt() and 0xff

            // Find the color that (almost) matches what we want
            var mostSimilarColor: Byte = 0
            var currentColorDistance = (256 * 256 * 256).toDouble()
            val isTransparencyColor = transparent?.red == r && transparent.green == g && transparent.blue == b

            for ((index, color) in colorsToBeWritten.withIndex()) {
                // Transparency should be 100% exact!
                if ((isTransparencyColor && color.red == r && color.green == g && color.blue == b)) {
                    mostSimilarColor = index.toByte()
                    transparentIndex = mostSimilarColor
                    break
                } else {
                    // https://stackoverflow.com/a/9085524/7271796
                    val rMean = (r.toLong() + color.red.toLong()) / 2
                    val rDist = r.toLong() - color.red.toLong()
                    val gDist = g.toLong() - color.green.toLong()
                    val bDist = b.toLong() - color.blue.toLong()
                    val distance =
                        sqrt(((512 + rMean) * rDist * rDist shr 8) + 4 * gDist * gDist + ((767 - rMean) * bDist * bDist shr 8).toDouble())

                    if (currentColorDistance > distance) {
                        currentColorDistance = distance
                        mostSimilarColor = index.toByte()
                        if (distance == 0.0) // They are equal, so we don't need to do any more checks here :)
                            break
                    }
                }
            }

            indexedPixels[i] = mostSimilarColor
        }

        // Get closest match to transparent color if specified and if the current frame has transparent pixels
        // We check if they are present to avoid issues if the frame only has a solid color without any transparency
        val transIndex = if (hasTransparentPixels) {
            transparentIndex.toInt()
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