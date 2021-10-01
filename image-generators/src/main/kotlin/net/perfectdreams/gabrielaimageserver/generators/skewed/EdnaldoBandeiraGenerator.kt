package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class EdnaldoBandeiraGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        44f, 220f,
        480f, 174f,
        340f, 516f,
        55f, 498f
    )
)