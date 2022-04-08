package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

interface SingleSourceImageToImageGenerator : SingleSourceImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val result = generateImage(source)
        val output = ByteArrayOutputStream()
        ImageIO.write(result, "png", output)
        return output.toByteArray()
    }

    fun generateImage(source: BufferedImage): BufferedImage
}