package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class SAMGenerator(val samLogo: Image) {
    fun generate(source: Image, xPercentage: Double, yPercentage: Double): Image {
        // The SAM logo needs to be 1/4 of the original image
        // (So we divide by 2... which is half the size)
        val logoScalePercentage: Double
        if (source.width > source.height) {
            logoScalePercentage = (source.width / 2.0) / samLogo.width.toDouble()
        } else {
            logoScalePercentage = (source.height / 2.0) / samLogo.height.toDouble()
        }

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