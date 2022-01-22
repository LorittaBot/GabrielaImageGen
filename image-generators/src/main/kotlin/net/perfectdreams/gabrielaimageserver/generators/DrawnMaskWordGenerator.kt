package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import java.awt.Color
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.image.BufferedImage

class DrawnMaskWordGenerator(
    val drawnMaskWordImage: BufferedImage,
    val drawnMaskWordBottomImage: BufferedImage,
    val babyMaskChairImage: BufferedImage
) : Generator {
    fun generate(text: String): BufferedImage {
        val text = text.take(800)

        fun getTextWrapSpacesRequiredHeight(text: String, startX: Int, startY: Int, endX: Int, endY: Int, fontMetrics: FontMetrics, graphics: Graphics): Int {
            val lineHeight = fontMetrics.height

            var currentX = startX
            var currentY = startY

            val split = text.split("((?<= )|(?= ))".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (str in split) {
                var width = fontMetrics.stringWidth(str)
                if (currentX + width > endX) {
                    currentX = startX
                    currentY += lineHeight
                }
                var idx = 0
                for (c in str.toCharArray()) {
                    idx++
                    if (c == '\n') {
                        currentX = startX
                        currentY += lineHeight
                        continue
                    }
                    width = fontMetrics.charWidth(c)
                    /* if (!graphics.font.canDisplay(c)) {
                        val emoteImage = ImageUtils.getTwitterEmoji(str, idx)
                        if (emoteImage != null) {
                            currentX += width
                        }

                        continue
                    } */

                    currentX += width
                }
            }
            return currentY
        }

        var wordScreenHeight = drawnMaskWordImage.height

        val width = 468

        val graphics = drawnMaskWordImage.createGraphics().enableFontAntialiasing()

        val font2 = graphics.font.deriveFont(24f)
        graphics.font = font2
        val fontMetrics = graphics.fontMetrics

        val lineHeight = fontMetrics.height
        val startY = 90

        val currentY = getTextWrapSpacesRequiredHeight(
            text,
            54,
            90,
            drawnMaskWordImage.width,
            99999,
            fontMetrics,
            graphics
        )

        val currentJumps = (currentY - startY) / lineHeight

        val pixelsNeeded = currentY - startY

        if (currentJumps > 4) {
            val overflownPixels = (pixelsNeeded - (lineHeight * 3)) + lineHeight + lineHeight
            val requiredPastes = (overflownPixels / 53)

            wordScreenHeight += (53 * requiredPastes) - 27
        }

        val wordScreen = BufferedImage(drawnMaskWordImage.width, wordScreenHeight, BufferedImage.TYPE_INT_ARGB)
        val wordScreenGraphics = wordScreen.createGraphics().enableFontAntialiasing()

        wordScreenGraphics.drawImage(drawnMaskWordImage, 0, 0, null)

        wordScreenGraphics.color = Color.BLACK
        wordScreenGraphics.font = font2

        val fontMetrics2 = wordScreenGraphics.fontMetrics

        if (currentJumps > 4) {
            val overflownPixels = (pixelsNeeded - (lineHeight * 3)) + lineHeight + lineHeight
            val requiredPastes = (overflownPixels / 53)

            var currentY = (drawnMaskWordImage.height - 40)

            repeat(requiredPastes) {
                wordScreenGraphics.drawImage(drawnMaskWordBottomImage, 0, currentY, null)

                currentY += 53
            }
        }

        ImageUtils.drawTextWrapSpaces(
            text,
            54,
            90,
            wordScreen.width,
            99999,
            fontMetrics2,
            wordScreenGraphics
        )

        val image = BufferedImage(width, wordScreen.height + 202, BufferedImage.TYPE_INT_ARGB)
        val imageGraphics = image.graphics

        imageGraphics.fillRect(0, 0, 468, wordScreen.height + 202)
        imageGraphics.drawImage(babyMaskChairImage, 0, image.height - babyMaskChairImage.height, null)
        imageGraphics.drawImage(wordScreen, 218, 0, null)

        return image
    }
}