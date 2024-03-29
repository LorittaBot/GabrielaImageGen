package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.toBufferedImage
import net.perfectdreams.gabrielaimageserver.utils.GifSequenceWriter
import net.perfectdreams.gabrielaimageserver.utils.Gifsicle
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.MemoryCacheImageOutputStream

class GetOverHereGenerator(
    val gifsicle: Gifsicle,
    val assetsFolder: File
) : SingleSourceImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val scaledTargetImage = source.getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH).toBufferedImage()

        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, 10, true, true)

        for (i in 0..52) {
            val file = File(assetsFolder, "scorpion_${i.toString().padStart(6, '0')}.png")
            if (file.exists()) {
                val ogImage = ImageIO.read(file)
                val image = BufferedImage(ogImage.width, ogImage.height, BufferedImage.TYPE_INT_ARGB)
                image.graphics.drawImage(ogImage, 0, 0, null)

                when (i) {
                    in 0..4 -> {
                        image.graphics.drawImage(scaledTargetImage, 9, 27, null)
                    }
                    5 -> {
                        image.graphics.drawImage(scaledTargetImage, 49, 27, null)
                    }
                    6 -> {
                        image.graphics.drawImage(scaledTargetImage, 124, 27, null)
                    }
                    7 -> {
                        image.graphics.drawImage(scaledTargetImage, 107, 27, null)
                    }
                    in 8..9 -> {
                        image.graphics.drawImage(scaledTargetImage, 118, 24, null)
                    }
                    10 -> {
                        image.graphics.drawImage(scaledTargetImage, 85, 12, null)
                    }
                }
                gifWriter.writeToSequence(image)
            }
        }

        baosAsMemoryCacheImage.close()
        val endResult = baos.toByteArray()
        baos.close()

        return gifsicle.optimizeGIF(endResult)
    }
}