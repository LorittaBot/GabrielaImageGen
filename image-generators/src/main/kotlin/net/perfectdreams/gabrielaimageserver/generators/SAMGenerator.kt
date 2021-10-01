package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage

class SAMGenerator(val samLogo: BufferedImage) : Generator {
    fun generate(source: BufferedImage, xPercentage: Double, yPercentage: Double): BufferedImage {
        // The SAM logo needs to be 1/4 of the original image
        // (So we divide by 2... which is half the size)
        val logoScalePercentage: Double = if (source.width > source.height) {
            (source.width / 2.0) / samLogo.width.toDouble()
        } else {
            (source.height / 2.0) / samLogo.height.toDouble()
        }

        val scaledSAMLogo = samLogo.getScaledBufferedImageInstance(
            (samLogo.width * logoScalePercentage).toInt(),
            (samLogo.height * logoScalePercentage).toInt(),
            BufferedImage.SCALE_SMOOTH
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