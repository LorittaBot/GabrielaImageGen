package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

abstract class BasicSkewedImageGenerator(
    val template: BufferedImage,
    val corners: List<Corners>
) : SingleSourceImageToImageGenerator {
    constructor(template: BufferedImage, corner: Corners) : this(template, listOf(corner))

    override fun generateImage(source: BufferedImage): BufferedImage {
        // Create a base image
        val newImage = createImage(template.width, template.height)
        val graphics = newImage.createGraphics()

        // Scale the source image to be the same size as the template
        val scaledSource = source.getScaledBufferedImageInstance(template.width, template.height, BufferedImage.SCALE_SMOOTH)

        for (corner in corners) {
            graphics.drawImage(
                scaledSource
                    .getSkewedInstance(
                        corner.upperLeftX, corner.upperLeftY,
                        corner.upperRightX, corner.upperRightY,
                        corner.lowerRightX, corner.lowerRightY,
                        corner.lowerLeftX, corner.lowerLeftY
                    ),
                0,
                0
            )
        }

        graphics.drawImage(template, 0, 0)

        return newImage
    }
}