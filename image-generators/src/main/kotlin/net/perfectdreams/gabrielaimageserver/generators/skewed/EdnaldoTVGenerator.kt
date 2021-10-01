package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class EdnaldoTVGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        270f, 19f,
        411f, 21f,
        409f, 126f,
        272f, 128f
    )
)