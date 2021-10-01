package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class LoriSignGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        20f, 202f,
        155f, 226f,
        139f, 299f,
        3f, 275f
    )
)