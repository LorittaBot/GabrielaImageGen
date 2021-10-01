package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class GessyAtaGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        130F, 35F,
        410F, 92F,
        387F, 277F,
        111F, 210F
    )
)