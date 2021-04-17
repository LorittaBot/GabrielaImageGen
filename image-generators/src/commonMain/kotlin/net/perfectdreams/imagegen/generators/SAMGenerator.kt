package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class SAMGenerator(val samLogo: Image) {
    fun generate(source: Image, xPercentage: Double, yPercentage: Double): Image {
        // The SAM logo needs to be 1/4 of the original image
        // (So we divide by 2... which is half the size)
        val isHeightSmaller = source.width > source.height
        val smallestSide = (if (isHeightSmaller) source.height else source.width) / 2

        // If the size is smaller than zero, just return the source image
        if (0 >= smallestSide)
            return source

        val logoScalePercentage = if (isHeightSmaller)
            smallestSide / source.height.toDouble()
        else
            smallestSide / source.width.toDouble()

        val scaledSAMLogo = samLogo.getScaledInstance(
            (samLogo.width * logoScalePercentage).toInt(),
            (samLogo.height * logoScalePercentage).toInt(),
            Image.ScaleType.SMOOTH
        )

        // Now paste somewhere!
        source.createGraphics().drawImage(
            scaledSAMLogo,
            ((xPercentage * source.width) - (scaledSAMLogo.width / 2)).toInt(),
            ((yPercentage * source.height) - (scaledSAMLogo.height / 2)).toInt()
        )

        return source
    }
}