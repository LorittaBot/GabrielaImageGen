package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

interface TwoSourceImagesToImageGenerator : TwoSourceImagesToByteArrayGenerator {
    override fun generate(source1: BufferedImage, source2: BufferedImage): ByteArray {
        val result = generateImage(source1, source2)
        val output = ByteArrayOutputStream()
        ImageIO.write(result, "png", output)
        return output.toByteArray()
    }

    fun generateImage(source1: BufferedImage, source2: BufferedImage): BufferedImage
}