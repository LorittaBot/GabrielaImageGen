package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage

abstract class BasicScaledImageGenerator(
    val template: BufferedImage,
    val scaleXTo: Int,
    val scaleYTo: Int,
    val x: Int,
    val y: Int,
) : SingleSourceImageToImageGenerator {
    override fun generateImage(source: BufferedImage): BufferedImage {
        // Create a base image
        val newImage = createImage(template.width, template.height)
        val graphics = newImage.createGraphics()

        val scaled = source.getScaledInstance(scaleXTo, scaleYTo, BufferedImage.SCALE_SMOOTH)

        graphics.drawImage(scaled, x, y)
        graphics.drawImage(template, 0, 0)

        return newImage
    }
}