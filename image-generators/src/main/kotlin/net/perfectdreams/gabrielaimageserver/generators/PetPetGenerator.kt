package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.utils.GifSequenceWriter
import net.perfectdreams.gabrielaimageserver.utils.Gifsicle
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.MemoryCacheImageOutputStream

class PetPetGenerator(
    val gifsicle: Gifsicle,
    assetsFolder: File
) : Generator {
    // Based on https://benisland.neocities.org/petpet/
    // Thx ben!
    private val handPatSprites = mutableListOf<BufferedImage>()

    init {
        val sprites = ImageIO.read(File(assetsFolder, "sprite.png"))

        repeat(5) {
            handPatSprites += sprites.getSubimage(112 * it, 0, 112, 112)
        }
    }

    /**
     * Frame offset values
     */
    private val frameOffsets = listOf(
        GraphicFrame(
            0,
            0,
            0,
            0
        ),
        GraphicFrame(
            -4,
            12,
            4,
            -12
        ),
        GraphicFrame(
            -12,
            18,
            12,
            -18
        ),
        GraphicFrame(
            -8,
            12,
            4,
            -12
        ),
        GraphicFrame(
            -4,
            0,
            0,
            0
        )
    )

    /**
     * Get the sprite's positioning for a frame
     * @param frame the current frame
     */
    private fun getSpriteFrame(frame: Int, options: PetPetOptions): GraphicFrame {
        val offset = frameOffsets[frame]

        return GraphicFrame(
            (Defaults.spriteX + offset.x * (options.squish * 0.4)).toInt(),
            (Defaults.spriteY + offset.y * (options.squish * 0.9)).toInt(),
            ((Defaults.spriteWidth + offset.w * options.squish) * Defaults.scale).toInt(),
            ((Defaults.spriteHeight + offset.h * options.squish) * Defaults.scale).toInt()
        )
    }

    fun generate(source: BufferedImage, options: PetPetOptions): ByteArray {
        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifWriter = GifSequenceWriter(baosAsMemoryCacheImage, BufferedImage.TYPE_INT_ARGB, options.delayBetweenFrames, true, true)

        repeat(frameOffsets.size) {
            val cf = getSpriteFrame(it, options)
            val new = BufferedImage(112, 112, BufferedImage.TYPE_INT_ARGB)

            val editedFrame = source.getScaledInstance(cf.w, cf.h, BufferedImage.SCALE_SMOOTH)

            new.graphics.drawImage(
                editedFrame,
                cf.x,
                cf.y,
                null
            )

            new.graphics.drawImage(
                handPatSprites[it],
                0,
                // okay ben I won't ask bb - don't ask where these numbers are from they just work....
                (cf.y * 0.75 - Defaults.spriteY.coerceAtLeast(0) - 0.5).toInt().coerceAtLeast(0),
                null
            )

            gifWriter.writeToSequence(new)
        }

        baosAsMemoryCacheImage.close()
        val endResult = baos.toByteArray()
        baos.close()

        return gifsicle.optimizeGIF(endResult)
    }

    data class GraphicFrame(
        val x: Int,
        val y: Int,
        val w: Int,
        val h: Int
    )

    data class PetPetOptions(
        val squish: Double,
        val delayBetweenFrames: Int,
    )

    // Those values are from Ben's original generator
    private object Defaults {
        // val squish = 1.25
        val scale = 0.875
        // val delay = 60
        val spriteX = 14
        val spriteY = 20
        val spriteWidth = 112
        val spriteHeight = 112
        // val currentFrame = 0
        // val flip = false
    }
}