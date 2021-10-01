package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class ChicoAtaGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        300F, 0F,
        768F, 0F,
        768F, 480F,
        300F, 383F
    )
)