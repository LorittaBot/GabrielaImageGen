package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class LoriAtaGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        273F, 0F,
        768F, 0F,
        768F, 454F,
        245F, 354F
    )
)