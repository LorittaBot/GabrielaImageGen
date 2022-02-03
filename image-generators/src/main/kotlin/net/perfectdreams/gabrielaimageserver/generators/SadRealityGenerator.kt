package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage

class SadRealityGenerator(
    val font: Font
) : Generator {
    fun generateImage(
        text1: String,
        text2: String,
        text3: String,
        text4: String,
        text5: String,
        text6: String,
        image1: BufferedImage,
        image2: BufferedImage,
        image3: BufferedImage,
        image4: BufferedImage,
        image5: BufferedImage,
        image6: BufferedImage
    ): BufferedImage {
        var x = 0
        var y = 0

        val base = BufferedImage(384, 256, BufferedImage.TYPE_INT_ARGB) // Iremos criar uma imagem 384x256 (tamanho do template)
        val baseGraph = base.createGraphics().enableFontAntialiasing()

        val results = listOf(
            Pair(text1, image1),
            Pair(text2, image2),
            Pair(text3, image3),
            Pair(text4, image4),
            Pair(text5, image5),
            Pair(text6, image6)
        )

        for ((text, image) in results) {
            baseGraph.drawImage(image, x, y, null)

            /* baseGraph.font = Constants.MINECRAFTIA.deriveFont(Font.PLAIN, 8f)
            baseGraph.color = Color.BLACK
            baseGraph.drawString(member.name + "#" + member.handle.discriminator, x + 1, y + 12)
            baseGraph.drawString(member.name + "#" + member.handle.discriminator, x + 1, y + 14)
            baseGraph.drawString(member.name + "#" + member.handle.discriminator, x, y + 13)
            baseGraph.drawString(member.name + "#" + member.handle.discriminator, x + 2, y + 13)
            baseGraph.color = Color.WHITE
            baseGraph.drawString(member.name + "#" + member.handle.discriminator, x + 1, y + 13) */

            baseGraph.font = font.deriveFont(22f)

            drawCentralizedTextOutlined(
                baseGraph,
                text,
                Rectangle(x, y + 80, 128, 42),
                Color.WHITE,
                Color.BLACK,
                2
            )

            x += 128
            if (x > 256) {
                x = 0
                y = 128
            }
        }

        return base
    }

    private fun drawCentralizedTextOutlined(graphics: Graphics, text: String, rectangle: Rectangle, fontColor: Color, strokeColor: Color, strokeSize: Int) {
        val font = graphics.font
        graphics.font = font
        val fontMetrics = graphics.fontMetrics

        val lines = mutableListOf<String>()

        val split = text.split(" ")

        var x = 0
        var currentLine = StringBuilder()

        for (string in split) {
            val stringWidth = fontMetrics.stringWidth("$string ")
            val newX = x + stringWidth

            if (newX >= rectangle.width) {
                var endResult = currentLine.toString().trim()
                if (endResult.isEmpty()) { // okay wtf
                    // Se o texto é grande demais e o conteúdo atual está vazio... bem... substitua o endResult pela string atual
                    endResult = string
                    lines.add(endResult)
                    x = 0
                    continue
                }
                lines.add(endResult)
                currentLine = StringBuilder()
                currentLine.append(' ')
                currentLine.append(string)
                x = fontMetrics.stringWidth("$string ")
            } else {
                currentLine.append(' ')
                currentLine.append(string)
                x = newX
            }
        }
        lines.add(currentLine.toString().trim())

        val skipHeight = fontMetrics.ascent
        var y = (rectangle.height / 2) - ((skipHeight - 4) * (lines.size - 1))
        for (line in lines) {
            graphics.color = strokeColor
            for (strokeX in rectangle.x - strokeSize .. rectangle.x + strokeSize) {
                for (strokeY in rectangle.y + y - strokeSize .. rectangle.y + y + strokeSize) {
                    ImageUtils.drawCenteredString(graphics, line, Rectangle(strokeX, strokeY, rectangle.width, 24))
                }
            }
            graphics.color = fontColor
            ImageUtils.drawCenteredString(graphics, line, Rectangle(rectangle.x, rectangle.y + y, rectangle.width, 24))
            y += skipHeight
        }
    }
}