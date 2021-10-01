package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.NoCopyByteArrayOutputStream
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

interface SingleSourceImageToImageGenerator : SingleSourceImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val result = generateImage(source)
        val output = NoCopyByteArrayOutputStream()
        ImageIO.write(result, "png", output)
        return output.toByteArray()
    }

    fun generateImage(source: BufferedImage): BufferedImage
}