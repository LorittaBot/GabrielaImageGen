package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.LorittaImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.GifSequenceWriter
import net.perfectdreams.imageserver.utils.extensions.toBufferedImage
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.stream.MemoryCacheImageOutputStream

class NichijouYuukoPaperGenerator(
    val m: GabrielaImageGen,
    val assetsFolder: File
) {
    fun generate(targetImage: BufferedImage): ByteArray {
        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, 10, true, true)

        // Those images are used in the GIF generation loop
        // Because they never change, we will pre-edit them, giving a smol performance boost!
        // (And less memory garbage generated!)
        val section1Scaled = targetImage.getScaledInstance(400, 224, BufferedImage.SCALE_SMOOTH).toBufferedImage()
        val section1Transformed = LorittaImage(section1Scaled)
        section1Transformed.setCorners(131F, 55F,
            252F, 16F,
            308F, 186F,
            190F, 223F)
        val section1Image = section1Transformed.bufferedImage

        val scaled2Image = targetImage.getScaledInstance(500, 400, BufferedImage.SCALE_SMOOTH).toBufferedImage()
        val scaled2Transformed = LorittaImage(scaled2Image)
        scaled2Transformed.setCorners(56F, 66F,
            297F, 0F,
            412F, 304F,
            154F, 348F)
        scaled2Transformed.crop(0, 20, 400, 224)
        val section2Image = scaled2Transformed.bufferedImage

        // Same as above: Keep the overlay pre-loaded because they don't change between frames
        val overlay1 = ImageIO.read(File(assetsFolder, "mention_overlay1.png"))
        val overlay2 = ImageIO.read(File(assetsFolder, "mention_overlay2.png"))

        // The only thing we keep on the disk is the frames themselves, because keeping them in memory would use tons of memory

        for (i in 0..83) {
            val file = File(assetsFolder, "/base/mention_${i.toString().padStart(6, '0')}.png")
            if (file.exists()) {
                val ogImage = ImageIO.read(file)
                val image = BufferedImage(ogImage.width, ogImage.height, BufferedImage.TYPE_INT_ARGB)
                image.graphics.drawImage(ogImage, 0, 0, null)

                if (i in 51..58) {
                    image.graphics.drawImage(section1Image, 0, 0, null)
                    image.graphics.drawImage(overlay1, 0, 0, null)
                }

                if (i in 59..65) {
                    image.graphics.drawImage(section2Image, 0, 0, null)
                    image.graphics.drawImage(overlay2, 0, 0, null)
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