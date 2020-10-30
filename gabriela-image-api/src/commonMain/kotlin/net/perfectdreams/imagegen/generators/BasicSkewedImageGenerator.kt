package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.createImage

abstract class BasicSkewedImageGenerator(
    val template: Image,
    val corners: List<Corners>
) {
    constructor(template: Image, corner: Corners) : this(template, listOf(corner))

    fun generate(source: Image): Image {
        // Create a base image
        val newImage = createImage(template.width, template.height)
        val graphics = newImage.createGraphics()

        // Scale the source image to be the same size as the template
        val scaledSource = source.getScaledInstance(template.width, template.height, Image.ScaleType.SMOOTH)

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