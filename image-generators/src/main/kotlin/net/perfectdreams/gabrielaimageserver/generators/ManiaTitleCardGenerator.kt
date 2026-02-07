package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.toBufferedImage
import java.awt.Color
import java.awt.image.BufferedImage

class ManiaTitleCardGenerator(
    val image: BufferedImage,
    val left: BufferedImage,
    val right: BufferedImage,
    val characters: Map<Char, BufferedImage>
) : Generator {
    fun generate(text1: String, text2: String?): BufferedImage {
        val image = ImageUtils.deepCopy(image)
        val graphics = image.graphics
        graphics.color = Color.BLACK

        val bottomText = text2 ?: text1
        val topText = if (text2 == null) "" else text1

        run {
            var x = 516

            val y = 336 - 60

            graphics.drawImage(right, x, y + right.height, null)

            for (c in bottomText.reversed()) {
                if (c == ' ') {
                    graphics.fillRect(x - 26, y + 34, 26 + 2, 34)
                    x -= 26
                    continue
                }
                val charImg = characters[c.lowercaseChar()]

                if (charImg != null) {
                    val scaledCharImg = charImg.getScaledInstance(charImg.width * 2, charImg.height * 2, BufferedImage.SCALE_FAST).toBufferedImage()

                    graphics.fillRect(x - scaledCharImg.width, y + 34, scaledCharImg.width + 2, 34)
                    graphics.drawImage(scaledCharImg, x - scaledCharImg.width, y, null)
                    x -= scaledCharImg.width + 2
                }
            }
            graphics.drawImage(left, x - left.width + 2, y + left.height, null)
        }

        if (topText.isNotEmpty()) {
            var x = 516

            val y = 257 - 60

            graphics.drawImage(right, x, y + right.height, null)

            for (c in topText.reversed()) {
                if (c == ' ') {
                    graphics.fillRect(x - 26, y + 34, 26 + 2, 34)
                    x -= 26
                    continue
                }
                val charImg = characters[c.lowercaseChar()]

                if (charImg != null) {
                    val scaledCharImg = charImg.getScaledInstance(charImg.width * 2, charImg.height * 2, BufferedImage.SCALE_FAST).toBufferedImage()

                    graphics.fillRect(x - scaledCharImg.width, y + 34, scaledCharImg.width + 2, 34)
                    graphics.drawImage(scaledCharImg, x - scaledCharImg.width, y, null)
                    x -= scaledCharImg.width + 2
                }
            }
            graphics.drawImage(left, x - left.width + 2, y + left.height, null)
        }

        return image
    }
}