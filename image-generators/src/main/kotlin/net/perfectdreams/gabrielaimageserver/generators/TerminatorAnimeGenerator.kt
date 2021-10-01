package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.drawStringWithOutline
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage

class TerminatorAnimeGenerator(
    val template: BufferedImage,
    val font: Font
) : Generator {
    fun generate(input1: String, input2: String): BufferedImage {
        val terminatorAnime = ImageUtils.deepCopy(template)

        val graphics = terminatorAnime.createGraphics()
            .enableFontAntialiasing()

        graphics.color = Color(255, 251, 0)
        graphics.font = font

        fun drawTextCentralizedNewLines(text: String, startAtX: Int, startAtY: Int) {
            val startAtX = startAtX
            var startAtY = startAtY

            val splitInput1 = text.split("((?<= )|(?= ))".toRegex()).dropLastWhile { it.isEmpty() }
            var input1FitInLine = ""

            for (split in splitInput1) {
                val old = input1FitInLine

                input1FitInLine += split

                println("${startAtX - (graphics.getFontMetrics(font).stringWidth(old) / 2)}")
                if (0 >= startAtX - (graphics.getFontMetrics(font).stringWidth(input1FitInLine) / 2) || startAtX + (graphics.getFontMetrics(font).stringWidth(input1FitInLine) / 2) >= terminatorAnime.width) {
                    println((graphics.getFontMetrics(font).stringWidth(old)))

                    val drawAtX = startAtX - (graphics.getFontMetrics(font).stringWidth(old) / 2)
                    graphics.drawStringWithOutline(old, drawAtX, startAtY, Color.BLACK, 2)
                    startAtY += 26
                    input1FitInLine = ""
                    input1FitInLine += split
                }
            }

            val drawAtX = startAtX - (graphics.getFontMetrics(font).stringWidth(input1FitInLine) / 2)
            graphics.drawStringWithOutline(input1FitInLine, drawAtX, startAtY, Color.BLACK, 2)
        }

        val centerInput1X = 98
        val centerInput1Y = 138
        val centerInput2X = 286
        val centerInput2Y = 254

        drawTextCentralizedNewLines(input1, centerInput1X, centerInput1Y)
        drawTextCentralizedNewLines(input2, centerInput2X, centerInput2Y)

        return terminatorAnime
    }
}