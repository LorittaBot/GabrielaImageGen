package net.perfectdreams.imageserver.generators

import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.utils.GifSequenceWriter
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.MemoryCacheImageOutputStream

class PetPetGenerator(
    val m: GabrielaImageGen,
    assetsFolder: File
) {
    // Based on https://benisland.neocities.org/petpet/
    // Thx ben!

    val handPatSprites = mutableListOf<BufferedImage>()

    init {
        val sprites = ImageIO.read(File(assetsFolder, "sprite.png"))

        repeat(5) {
            handPatSprites += sprites.getSubimage(112 * it, 0, 112, 112)
        }
    }

    private val baseFrame = GraphicFrame(
        18,
        18,
        112,
        112
    )
    private val scale = 0.875
    private val spritePositions = listOf(
        GraphicFrame(
            baseFrame.x,
            baseFrame.y,
            (baseFrame.w * scale).toInt(),
            (baseFrame.h * scale).toInt()
        ),
        GraphicFrame(
            baseFrame.x - 4,
            baseFrame.y + 12,
            (baseFrame.w * scale).toInt() + 4,
            (baseFrame.h * scale).toInt() - 12
        ),
        GraphicFrame(
            baseFrame.x - 12,
            baseFrame.y + 18,
            (baseFrame.w * scale).toInt() + 12,
            (baseFrame.h * scale).toInt() - 18
        ),
        GraphicFrame(
            baseFrame.x - 12,
            baseFrame.y + 12,
            (baseFrame.w * scale).toInt() + 4,
            (baseFrame.h * scale).toInt() - 12
        ),
        GraphicFrame(
            baseFrame.x - 4,
            baseFrame.y,
            (baseFrame.w * scale).toInt(),
            (baseFrame.h * scale).toInt()
        )
    )
    
    fun generate(targetImage: BufferedImage): ByteArray {
        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, 7, true, true)

        for ((idx, frame) in spritePositions.withIndex()) {
            val new = BufferedImage(112, 112, BufferedImage.TYPE_INT_ARGB)

            val editedFrame = targetImage.getScaledInstance(frame.w, frame.h, BufferedImage.SCALE_SMOOTH)
            new.graphics.drawImage(editedFrame, frame.x, frame.y, null)
            new.graphics.drawImage(handPatSprites[idx], 0, 0, null)

            gifWriter.writeToSequence(new)
        }

        baosAsMemoryCacheImage.close()
        val endResult = baos.toByteArray()
        baos.close()

        return m.gifsicle.optimizeGIF(endResult)
    }

    data class GraphicFrame(
        val x: Int,
        val y: Int,
        val w: Int,
        val h: Int
    )
}