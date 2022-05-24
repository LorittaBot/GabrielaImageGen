package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage

class ColorInfoGenerator(
    val font: Font,
) : Generator {
    companion object {
        private val FACTOR = 0.7
    }

    fun generate(
        color: Color,
        triadColor1: Color,
        triadColor2: Color,
        analogousColor1: Color,
        analogousColor2: Color,
        complementaryColor: Color,
        shades: String,
        tints: String,
        triadic: String,
        analogous: String,
        complementary: String
    ): BufferedImage {
        fun Graphics.drawWithOutline(text: String, x: Int, y: Int) {
            this.color = Color.BLACK
            this.drawString(text, x - 1, y)
            this.drawString(text, x + 1, y)
            this.drawString(text, x, y - 1)
            this.drawString(text, x, y + 1)
            this.color = Color.WHITE
            this.drawString(text, x, y)
        }

        fun Graphics.drawColor(color: Color, x: Int, y: Int) {
            this.color = color
            this.fillRect(x, y, 48, 48)

            val hex = String.format("#%02x%02x%02x", color.red, color.green, color.blue)

            var _x = x + 48

            for (char in hex) {
                _x -= this.fontMetrics.charWidth(char)
            }

            this.drawWithOutline(hex, _x - 1, y + 48 - 2)
        }

        val colorInfo = BufferedImage(333, 250, BufferedImage.TYPE_INT_ARGB)
        val graphics = colorInfo.graphics

        val font = font.deriveFont(10f)

        graphics.font = font

        graphics.drawWithOutline(shades, 2, 11 + 1)

        run {
            var shade = Color(color.rgb)
            var previousShade: Int? = null
            var x = 0

            while (previousShade != shade.rgb) {
                graphics.color = shade
                graphics.drawColor(shade, x, 13)

                val newR = shade.red * (1 - FACTOR)
                val newG = shade.green * (1 - FACTOR)
                val newB = shade.blue * (1 - FACTOR)

                previousShade = shade.rgb
                shade = Color(newR.toInt(), newG.toInt(), newB.toInt())
                x += 48
            }
        }

        graphics.drawWithOutline(tints, 2, 13 + 48 + 9 + 1)

        run {
            var tint = Color(color.rgb)
            var previousTint: Int? = null
            var x = 0

            while (previousTint != tint.rgb) {
                graphics.color = tint
                graphics.drawColor(tint, x, 13 + 48 + 9 + 2)

                val newR = tint.red + (255 - tint.red) * FACTOR
                val newG = tint.green + (255 - tint.green) * FACTOR
                val newB = tint.blue + (255 - tint.blue) * FACTOR

                previousTint = tint.rgb
                tint = Color(newR.toInt(), newG.toInt(), newB.toInt())
                x += 48
            }
        }

        graphics.drawWithOutline(triadic, 2, 13 + 48 + 9 + 48 + 11 + 1)

        graphics.drawColor(color, 0, 13 + 48 + 9 + 48 + 11 + 4)
        graphics.drawColor(triadColor1, 48, 13 + 48 + 9 + 48 + 11 + 4)
        graphics.drawColor(triadColor2, 96, 13 + 48 + 9 + 48 + 11 + 4)

        graphics.drawWithOutline(analogous, 2, 13 + 48 + 9 + 48 + 11 + 48 + 11 + 3 + 1)

        graphics.drawColor(analogousColor1, 0, 13 + 48 + 9 + 48 + 11 + 48 + 15 + 3)
        graphics.drawColor(analogousColor2, 48, 13 + 48 + 9 + 48 + 11 + 48 + 15 + 3)

        graphics.drawWithOutline(complementary, 146, 13 + 48 + 9 + 48 + 11 + 1)

        graphics.drawColor(color, 146, 13 + 48 + 9 + 48 + 11 + 4)
        graphics.drawColor(complementaryColor, 194, 13 + 48 + 9 + 48 + 11 + 4)

        val colorPreview = BufferedImage(192, 192, BufferedImage.TYPE_INT_ARGB)
        val previewGraphics = colorPreview.graphics
        previewGraphics.color = color
        previewGraphics.fillRect(0, 0, 192, 192)

        graphics.drawImage(ImageUtils.makeRoundedCorners(colorPreview, 99999), 237, 167, null)

        return colorInfo
    }
}