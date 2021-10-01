package net.perfectdreams.gabrielaimageserver.generators

import java.awt.Color
import java.awt.image.BufferedImage

class ToBeContinuedGenerator(
    val toBeContinuedArrow: BufferedImage
) : SingleSourceImageToImageGenerator {
    override fun generateImage(source: BufferedImage): BufferedImage {
        val sepiaEffectImage = BufferedImage(source.width, source.height, BufferedImage.TYPE_INT_ARGB)
        val sepiaEffectImageGraphics = sepiaEffectImage.createGraphics()

        for (x in 0 until source.width) {
            for (y in 0 until source.height) {
                val rgb = source.getRGB(x, y)

                val color = Color(rgb)

                // https://stackoverflow.com/questions/1061093/how-is-a-sepia-tone-created
                val sepiaRed = (color.red * .393) + (color.green *.769) + (color.blue * .189)
                val sepiaGreen = (color.red * .349) + (color.green *.686) + (color.blue * .168)
                val sepiaBlue = (color.red * .272) + (color.green *.534) + (color.blue * .131)

                sepiaEffectImage.setRGB(
                    x, y,
                    Color(
                        Math.min(
                            255,
                            sepiaRed.toInt()
                        ),
                        Math.min(
                            255,
                            sepiaGreen.toInt()
                        ),
                        Math.min(
                            255,
                            sepiaBlue.toInt()
                        )
                    ).rgb
                )
            }
        }

        // Em 1280x720
        // A margem entre a parte esquerda até a seta é 42 pixels
        // A margem entre a parte de baixo até a seta também é 42 pixels
        // A largura da seta é 664
        // Altura é 152
        val padding = (42 * sepiaEffectImage.width) / 1280

        val arrowWidth = (664 * sepiaEffectImage.width) / 1280
        val arrowHeight = (152 * arrowWidth) / 664

        sepiaEffectImageGraphics.drawImage(
            toBeContinuedArrow
                .getScaledInstance(
                    arrowWidth,
                    arrowHeight,
                    BufferedImage.SCALE_SMOOTH
                ),
            padding,
            sepiaEffectImage.height - padding - arrowHeight,
            null
        )

        return sepiaEffectImage
    }
}