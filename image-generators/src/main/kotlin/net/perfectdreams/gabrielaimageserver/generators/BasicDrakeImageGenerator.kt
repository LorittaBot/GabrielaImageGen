package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage

abstract class BasicDrakeImageGenerator(
    val template: BufferedImage,
    val scale: Int
) : TwoSourceImagesToImageGenerator {
    override fun generateImage(source1: BufferedImage, source2: BufferedImage): BufferedImage {
        // Clone the base template
        val clonedTemplate = template.clone()
        val graph = clonedTemplate.createGraphics()

        run {
            val image = source1.getScaledInstance(150 * scale, 150 * scale, BufferedImage.SCALE_SMOOTH)
            graph.drawImage(image, 150 * scale, 0)
        }

        run {
            val image = source2.getScaledInstance(150 * scale, 150 * scale, BufferedImage.SCALE_SMOOTH)
            graph.drawImage(image, 150 * scale, 150 * scale)
        }

        return clonedTemplate
    }
}