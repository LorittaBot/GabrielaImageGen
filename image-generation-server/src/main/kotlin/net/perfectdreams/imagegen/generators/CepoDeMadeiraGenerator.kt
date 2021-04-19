package net.perfectdreams.imagegen.generators

import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.GifSequenceWriter
import net.perfectdreams.imageserver.utils.extensions.toBufferedImage
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.MemoryCacheImageOutputStream

class CepoDeMadeiraGenerator(
    val m: GabrielaImageGen,
    val assetsFolder: File
) : SingleSourceBufferedImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val scaledTargetImage = source.getScaledInstance(45, 45, BufferedImage.SCALE_SMOOTH).toBufferedImage()

        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, 10, true, true)

        for (i in 0..112) {
            val file = File(assetsFolder, "/base/cepo_${i.toString().padStart(6, '0')}.png")

            if (file.exists()) {
                val ogImage = ImageIO.read(file)
                val image = BufferedImage(ogImage.width, ogImage.height, BufferedImage.TYPE_INT_ARGB)

                image.graphics.drawImage(ogImage, 0, 0, null)

                if (i in 0..16) {
                    image.graphics.drawImage(scaledTargetImage, 65, 151, null)
                }

                if (i in 17..26) {
                    val fogoFx = i % 17
                    val fogo = ImageIO.read(File(assetsFolder, "/fire/fogo_${fogoFx.toString().padStart(6, '0')}.png"))
                    image.graphics.drawImage(fogo, 55, 141, null)
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