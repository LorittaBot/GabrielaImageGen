package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class RomeroBrittoGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        16F,19F,
        201F,34F,
        208F,218F,
        52F, 294F
    )
)