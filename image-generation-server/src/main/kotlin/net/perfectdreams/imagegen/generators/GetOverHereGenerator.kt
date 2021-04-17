package net.perfectdreams.imagegen.generators

import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.GifSequenceWriter
import net.perfectdreams.imageserver.utils.extensions.toBufferedImage
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.stream.MemoryCacheImageOutputStream

class GetOverHereGenerator(
    val m: GabrielaImageGen,
    val assetsFolder: File
) {
    fun generate(targetImage: BufferedImage): ByteArray {
        val scaledTargetImage = targetImage.getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH).toBufferedImage()

        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, 7, true, true)

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

        return m.gifsicle.optimizeGIF(endResult)
    }
}