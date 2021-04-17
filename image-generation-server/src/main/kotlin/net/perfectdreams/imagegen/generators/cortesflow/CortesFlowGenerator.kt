package net.perfectdreams.imagegen.generators.cortesflow

import net.perfectdreams.imageserver.utils.ImageUtils
import net.perfectdreams.imageserver.utils.extensions.enableFontAntialiasing
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.ConvolveOp
import java.awt.image.Kernel

open class CortesFlowGenerator(
    val template: BufferedImage,
    val font: Font,
    val startTextX: Int,
    val participant: FlowParticipant,
    val source: String
) {
    companion object {
        val CONVOLVE_OP: ConvolveOp = run {
            println("Creating Convolve Op")
            val radius = 20
            val size = radius * 2 + 1
            val weight = 1.0f / (size * size)
            val data = FloatArray(size * size)

            for (i in data.indices) {
                data[i] = weight
            }

            val kernel = Kernel(size, size, data)
            ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null)
        }

        val LEFT_SIDE_TEXT = 374
        val RIGHT_SIDE_TEXT = 882
    }

    fun generate(text: String): BufferedImage {
        val backgroundTemplate = ImageUtils.deepCopy(template)
        val graphics = backgroundTemplate.createGraphics()
            .enableFontAntialiasing()

        val newImage = BufferedImage(
            backgroundTemplate.width,
            backgroundTemplate.height,
            BufferedImage.TYPE_INT_ARGB
        )

        val text = text.toUpperCase()

        // 8 --- 92f
        // y --- z
        val middleOfTheScreenY = (backgroundTemplate.height / 2) - graphics.fontMetrics.descent
        graphics.color = Color.WHITE

        val newImageGraphics = newImage.createGraphics()

        // We are now going to brute force a good font size

        for (fontSize in 92 downTo (92 - (4 * 15)) step 4) {
            try {
                val fontX = font.deriveFont(
                    fontSize.toFloat()
                )

                println("Using font size $fontSize")

                graphics.font = fontX
                newImageGraphics.font = fontX

                newImageGraphics.color = Color(
                    113,
                    227,
                    255,
                    180
                )

                drawTextCentralizedNewLines(
                    newImageGraphics,
                    newImage.width,
                    fontX,
                    text,
                    startTextX,
                    middleOfTheScreenY,
                    fontSize != (92 - (4 * 15))
                )

                graphics.drawImage(CONVOLVE_OP.filter(newImage, null), 0, 0, null)

                newImageGraphics.color = Color.WHITE
                drawTextCentralizedNewLines(
                    graphics,
                    backgroundTemplate.width,
                    fontX,
                    text,
                    startTextX,
                    middleOfTheScreenY,
                    fontSize != (92 - (4 * 15))
                )
                break
            } catch (e: TextTooBigException) {
                println("Exception with size $fontSize")
            }
        }

        return backgroundTemplate
    }

    fun drawTextCentralizedNewLines(graphics: Graphics, maxWidth: Int, font: Font, text: String, startAtX: Int, startAtY: Int, throwOnOverflow: Boolean) {
        val allStrings = mutableListOf<String>()

        var startAtX = startAtX
        var startAtY = startAtY

        val splitInput1 = text.split("((?<= )|(?= ))".toRegex()).dropLastWhile { it.isEmpty() }
        var input1FitInLine = ""

        val fontMetrics = graphics.fontMetrics

        for (split in splitInput1) {
            val old = input1FitInLine

            input1FitInLine += split

            if (0 >= startAtX - (graphics.getFontMetrics(font).stringWidth(input1FitInLine) / 2) || startAtX + (graphics.getFontMetrics(
                    font
                ).stringWidth(input1FitInLine) / 2) >= maxWidth) {
                // println((graphics.getFontMetrics(font).stringWidth(old)))

                val drawAtX = startAtX - (graphics.getFontMetrics(font).stringWidth(old.trim()) / 2)
                // graphics.drawString(old, drawAtX, startAtY)
                allStrings += old
                // startAtY += graphics.fontMetrics.height
                input1FitInLine = ""
                input1FitInLine += split
                input1FitInLine = input1FitInLine.trim()
            }
        }

        allStrings += input1FitInLine

        var drawAtY = (fontMetrics.maxAscent + startAtY) - (allStrings.size * (fontMetrics.maxAscent / 2))

        if (throwOnOverflow && 0 > drawAtY - fontMetrics.maxAscent)
            throw TextTooBigException()

        for (str in allStrings) {
            graphics.drawString(str, startAtX - (fontMetrics.stringWidth(str) / 2), drawAtY)
            drawAtY += fontMetrics.maxAscent
        }

        // val drawAtX = startAtX - (graphics.getFontMetrics(font).stringWidth(input1FitInLine) / 2)
        // drawAtY
        // graphics.drawString(input1FitInLine, drawAtX, startAtY)
    }

    class TextTooBigException : RuntimeException()
}