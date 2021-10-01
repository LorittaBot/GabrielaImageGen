package net.perfectdreams.gabrielaimageserver.utils.gifs.palettecreators

import java.awt.Color

/**
 * Naive [PaletteCreator] implementation by using the most common pixels in the palette, comparing colors by distance. Faster than [NeuQuantPaletteCreator] and has better quality than [NaivePaletteCreator], but still not very good.
 */
class NaiveDistancePaletteCreator(private val similarDistanceToDrop: Int = 16) : PaletteCreator {
    companion object {
        fun createPaletteFromRgbValues(
            pixels: ByteArray,
            colorsThatShouldBePresent: List<Color>,
            similarDistanceToDrop: Int,
            transparent: Color?,
            sizeTarget: Int
        ): ByteArray {
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

            var colorsToBeWritten = rgbValues.entries.sortedByDescending { it.value }
                .map { it.key }
                .toMutableList()

            val maxSize = sizeTarget

            repeat(colorsToBeWritten.size) {
                if (maxSize >= colorsToBeWritten.size)
                    return@repeat

                if (it + 1 > colorsToBeWritten.size)
                    return@repeat

                val color = colorsToBeWritten[it]
                val checkColors = colorsToBeWritten.drop(it)

                for (checkedColor in checkColors) {
                    val distance = NaivePaletteCreator.colorDistance(
                        color.red,
                        color.green,
                        color.blue,
                        checkedColor.red,
                        checkedColor.green,
                        checkedColor.blue
                    )

                    if (similarDistanceToDrop >= distance)
                        colorsToBeWritten.remove(checkedColor)
                }
            }

            // Add our "Colors to be Written" list to the beginning of the palette array
            colorsToBeWritten.addAll(0, colorsThatShouldBePresent)

            colorsToBeWritten = colorsToBeWritten.take(maxSize)
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

            return colorsToBeWrittenAndFlattened.toByteArray()
        }
    }

    override fun createPaletteFromPixels(
        pixels: ByteArray,
        indexedPixels: ByteArray,
        transparent: Color?,
        hasTransparentPixels: Boolean
    ): PaletteCreatorResult {
        val len = pixels.size

        val colorTab = createPaletteFromRgbValues(
            pixels,
            listOf(),
            similarDistanceToDrop,
            transparent,
            if (transparent != null)
                255
            else
                256
        )

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