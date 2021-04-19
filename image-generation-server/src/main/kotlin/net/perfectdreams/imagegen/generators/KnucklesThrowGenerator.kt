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
import javax.imageio.stream.MemoryCacheImageOutputStream

class KnucklesThrowGenerator(
    val m: GabrielaImageGen,
    val assetsFolder: File
) : SingleSourceBufferedImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, 10, true, true)

        val loriImage = LorittaImage(source)
        loriImage.resize(400, 225)

        val aux1 = loriImage.copy()
        aux1.setCorners(
            287f, 29f,
            391f, 48f,
            372f, 151f,
            269f, 133f
        )
        val auxBuf1 = aux1.bufferedImage

        val aux2 = loriImage.copy()
        aux2.setCorners(
            287f, 39f,
            391f, 58f,
            372f, 161f,
            269f, 143f
        )
        val auxBuf2 = aux2.bufferedImage

        // We scale it to 1x1 so we can get the average color of the target image
        val scaleForColor = source.getScaledInstance(1, 1, BufferedImage.SCALE_AREA_AVERAGING)
        val override = Color(scaleForColor.toBufferedImage().getRGB(0, 0))

        val overrideHsbVals = FloatArray(3)
        Color.RGBtoHSB(override.red, override.green, override.blue, overrideHsbVals)
        val overrideHue = overrideHsbVals[0] * 360

        for (i in 0..61) {
            val file = File(assetsFolder, "base/knuxthrow_${i.toString().padStart(6, '0')}.png")
            if (file.exists()) {
                val ogImage = ImageIO.read(file)
                val graphics = ogImage.graphics

                if (i in 2..19) {
                    graphics.drawImage(auxBuf1, 0, 0, null)
                }
                if (i == 20) {
                    graphics.drawImage(auxBuf2, 0, 0, null)
                }

                if (i == 21) {
                    for (x in 0 until ogImage.width) {
                        for (y in 0 until ogImage.height) {
                            val colorPacked = ogImage.getRGB(x, y)

                            val color = Color(colorPacked)

                            val hsbVals = FloatArray(3)
                            Color.RGBtoHSB(color.red, color.green, color.blue, hsbVals)

                            val hue = hsbVals[0] * 360
                            val saturation = hsbVals[1] * 100
                            val value = hsbVals[2] * 100

                            if (hue.toInt() in 39..45) {
                                val newColor = Color.HSBtoRGB(overrideHue / 360, saturation / 100, value / 100)
                                ogImage.setRGB(x, y, newColor)
                            }
                        }
                    }
                }

                val overlayFile = File(assetsFolder, "overlay/knuxthrow_overlay_${i.toString().padStart(6, '0')}.png")
                if (overlayFile.exists()) {
                    val overlay = ImageIO.read(overlayFile)
                    graphics.drawImage(overlay, 0, 0, null)
                }

                gifWriter.writeToSequence(ogImage)
            }
        }
        baosAsMemoryCacheImage.close()
        val endResult = baos.toByteArray()
        baos.close()

        return m.gifsicle.optimizeGIF(endResult)
    }
}