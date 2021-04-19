package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.createImage

abstract class BasicScaledImageGenerator(
    val template: Image,
    val scaleXTo: Int,
    val scaleYTo: Int,
    val x: Int,
    val y: Int,
) : SingleSourceImageGenerator {
    override fun generate(source: Image): Image {
        // Create a base image
        val newImage = createImage(template.width, template.height)
        val graphics = newImage.createGraphics()

        val scaled = source.getScaledInstance(scaleXTo, scaleYTo, Image.ScaleType.SMOOTH)

        graphics.drawImage(scaled, x, y)
        graphics.drawImage(template, 0, 0)

        return newImage
    }
}