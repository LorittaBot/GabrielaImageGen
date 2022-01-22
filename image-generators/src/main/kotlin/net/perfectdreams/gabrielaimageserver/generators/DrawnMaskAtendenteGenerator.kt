package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import net.perfectdreams.gabrielaimageserver.graphics.LorittaImage
import java.awt.Color
import java.awt.Font
import java.awt.Rectangle
import java.awt.font.TextAttribute
import java.awt.image.BufferedImage


class DrawnMaskAtendenteGenerator(val image: BufferedImage, val font: Font) : Generator {
    fun generate(text: String): BufferedImage {
        val template = ImageUtils.deepCopy(image)

        val width = 214
        val height = 131

        val templateGraphics = template.graphics

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics().enableFontAntialiasing()

        val attributes = mapOf(
            TextAttribute.SIZE to 16f,
            TextAttribute.TRACKING to -0.080f
        )

        val font = font.deriveFont(attributes)

        graphics.font = font
        graphics.color = Color.BLACK
        val fontMetrics = graphics.fontMetrics
        val lines = mutableListOf<String>()

        val split = text.split(" ")

        var x = 0
        var currentLine = StringBuilder()

        for (string in split) {
            val stringWidth = fontMetrics.stringWidth("$string ")
            val newX = x + stringWidth

            if (newX >= width) {
                var endResult = currentLine.toString().trim()
                if (endResult.isEmpty()) {
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
        var y = (height / 2) - ((skipHeight - 10) * (lines.size - 1))
        for (line in lines) {
            // TODO: emoji
            ImageUtils.drawCenteredString(graphics, line, Rectangle(0, y, width, 24))
            y += skipHeight
        }

        val loriImage = LorittaImage(image)
        loriImage.rotate(8.0)
        templateGraphics.drawImage(loriImage.bufferedImage, 46, -20, null)

        return template
    }
}