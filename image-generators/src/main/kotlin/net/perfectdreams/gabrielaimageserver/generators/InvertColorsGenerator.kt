package net.perfectdreams.gabrielaimageserver.generators

import java.awt.Color
import java.awt.image.BufferedImage

class InvertColorsGenerator : SingleSourceImageToImageGenerator {
    override fun generateImage(source: BufferedImage): BufferedImage {
        for (x in 0 until source.width) {
            for (y in 0 until source.height) {
                val rgba = source.getRGB(x, y)
                var col = Color(rgba, true)
                col = Color(
                    255 - col.red,
                    255 - col.green,
                    255 - col.blue)
                source.setRGB(x, y, col.rgb)
            }
        }

        return source
    }
}