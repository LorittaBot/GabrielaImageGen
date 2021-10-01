package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class MonicaAtaGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        107F, 0F,
        300F, 0F,
        300F, 177F,
        96F, 138F
    )
)