package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class ArtGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        75f, 215f,
        172f, 242f,
        106f, 399f,
        13f, 369f
    )
)