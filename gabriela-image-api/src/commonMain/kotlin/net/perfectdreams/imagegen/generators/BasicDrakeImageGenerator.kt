package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.createImage

abstract class BasicDrakeImageGenerator(
    val template: Image,
    val scale: Int
) : TwoSourceImagesGenerator {
    override fun generate(source1: Image, source2: Image): Image {
        // Clone the base template
        val clonedTemplate = template.clone()
        val graph = clonedTemplate.createGraphics()

        run {
            val image = source1.getScaledInstance(150 * scale, 150 * scale, Image.ScaleType.SMOOTH)
            graph.drawImage(image, 150 * scale, 0)
        }

        run {
            val image = source2.getScaledInstance(150 * scale, 150 * scale, Image.ScaleType.SMOOTH)
            graph.drawImage(image, 150 * scale, 150 * scale)
        }

        return clonedTemplate
    }
}