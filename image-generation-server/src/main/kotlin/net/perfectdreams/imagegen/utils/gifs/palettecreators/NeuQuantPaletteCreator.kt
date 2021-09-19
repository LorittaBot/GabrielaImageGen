package net.perfectdreams.imagegen.utils.gifs.palettecreators

import net.perfectdreams.imagegen.utils.gifs.NeuQuant
import java.awt.Color

/**
 * [PaletteCreator] implementation using [NeuQuant], good quality, but very slow
 */
class NeuQuantPaletteCreator(
    val sample: Int = 1 // default sample interval for quantizer
) : PaletteCreator {
    protected var usedEntry = BooleanArray(256) // active palette entries

    override fun createPaletteFromPixels(pixels: ByteArray, indexedPixels: ByteArray, transparent: Color?, hasTransparentPixels: Boolean): PaletteCreatorResult {
        val len = pixels.size
        val nPix = len / 3

        val nq = NeuQuant(pixels, len, sample)
        // initialize quantizer
        val colorTab = nq.process() // create reduced palette

        // convert map from BGR to RGB
        run {
            var i: Int = 0
            while (i < colorTab.size) {
                val temp: Byte = colorTab.get(i)
                colorTab[i] = colorTab.get(i + 2)
                colorTab[i + 2] = temp
                usedEntry[i / 3] = false
                i += 3
            }
        }

        // map image pixels to new palette
        var k = 0
        for (i in 0 until nPix) {
            val index = nq.map(pixels[k++].toInt() and 0xff, pixels[k++].toInt() and 0xff, pixels[k++].toInt() and 0xff)
            usedEntry[index] = true
            indexedPixels[i] = index.toByte()
        }

        // Get closest match to transparent color if specified and if the current frame has transparent pixels
        // We check if they are present to avoid issues if the frame only has a solid color without any transparency
        val transIndex = if (hasTransparentPixels) {
            findClosest(colorTab, transparent!!)
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

    /**
     * Returns index of palette color closest to c
     *
     */
    protected fun findClosest(colorTab: ByteArray, c: Color): Int {
        val r = c.red
        val g = c.green
        val b = c.blue
        var minpos = 0
        var dmin = 256 * 256 * 256
        val len = colorTab.size
        var i = 0
        while (i < len) {
            val dr: Int = r - (colorTab[i++].toInt() and 0xff)
            val dg: Int = g - (colorTab[i++].toInt() and 0xff)
            val db: Int = b - (colorTab[i].toInt() and 0xff)
            val d = dr * dr + dg * dg + db * db
            val index = i / 3

            if (usedEntry[index] && d < dmin) {
                dmin = d
                minpos = index
            }
            i++
        }
        return minpos
    }
}