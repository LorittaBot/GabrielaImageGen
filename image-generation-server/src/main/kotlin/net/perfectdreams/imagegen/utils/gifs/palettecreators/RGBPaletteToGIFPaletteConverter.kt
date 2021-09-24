package net.perfectdreams.imagegen.utils.gifs.palettecreators

import java.awt.Color

object RGBPaletteToGIFPaletteConverter {
    /**
     * @param pixels        the image pixels in BGR format
     * @param colorTab      the image color palette in RGB format
     * @param indexedPixels the image pixels that will contain the index of each color in the palette
     * @param transparent   the image's transparency color
     * @return the index of the transparent color, -1 if not found
     */
    fun convert(
        pixels: ByteArray,
        colorTab: ByteArray,
        indexedPixels: ByteArray,
        transparent: Color?
    ): Int {
        val len = pixels.size
        val nPix = len / 3

        // TODO: Use isTransparencyColor + transparentIndex
        var transparentIndex = -1
        var k = 0

        var cachedLastColorR: Int? = null
        var cachedLastColorG: Int? = null
        var cachedLastColorB: Int? = null
        var cachedLastIndex: Int? = null

        for (i in 0 until nPix) {
            val b = pixels[k++].toInt() and 0xff
            val g = pixels[k++].toInt() and 0xff
            val r = pixels[k++].toInt() and 0xff

            // Find the color that (almost) matches what we want
            var mostSimilarColor = 0
            var currentColorDistance = (256 * 256 * 256).toDouble()
            val isTransparencyColor = transparent?.red == r && transparent.green == g && transparent.blue == b

            var colorTabIdx = 0
            // First we will check the cached RGB color!
            if (cachedLastColorR != null && cachedLastColorG != null && cachedLastColorB != null && cachedLastIndex != null) {
                currentColorDistance = NaivePaletteCreator.colorDistance(r, g, b, cachedLastColorR, cachedLastColorG, cachedLastColorB)
                mostSimilarColor = cachedLastIndex
            }

            // If our cached distance gave us 0.0, then we don't need to process anything else, let's go!!!
            // This decreases the generation time in 1s! (from ~6500ms to ~5500ms)
            if (currentColorDistance != 0.0) {
                while (colorTab.size > colorTabIdx) {
                    val colorIdx = colorTabIdx / 3

                    val cTabR = colorTab[colorTabIdx++].toInt() and 0xff
                    val cTabG = colorTab[colorTabIdx++].toInt() and 0xff
                    val cTabB = colorTab[colorTabIdx++].toInt() and 0xff

                    // Transparency should be 100% exact!
                    if ((isTransparencyColor && cTabR == r && cTabG == g && cTabB == b)) {
                        mostSimilarColor = colorIdx
                        transparentIndex = mostSimilarColor

                        cachedLastColorR = cTabR
                        cachedLastColorG = cTabG
                        cachedLastColorB = cTabB
                        break
                    } else if (!isTransparencyColor) { // We don't want colors similar to the transparent color being chosen!
                        // TODO: This is very slow, but how could we improve its speed?
                        val distance = NaivePaletteCreator.colorDistance(r, g, b, cTabR, cTabG, cTabB)

                        if (currentColorDistance > distance) {
                            currentColorDistance = distance
                            mostSimilarColor = colorIdx

                            cachedLastColorR = cTabR
                            cachedLastColorG = cTabG
                            cachedLastColorB = cTabB

                            if (distance == 0.0) // They are equal, so we don't need to do any more checks here :)
                                break
                        }
                    }
                }
            }

            cachedLastIndex = mostSimilarColor
            indexedPixels[i] = mostSimilarColor.toByte()
        }

        return transparentIndex
    }
}