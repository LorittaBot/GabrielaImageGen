package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class BolsoFrameGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        314F, 36F,
        394F, 41F,
        385F, 156F,
        301F, 151F
    )
)