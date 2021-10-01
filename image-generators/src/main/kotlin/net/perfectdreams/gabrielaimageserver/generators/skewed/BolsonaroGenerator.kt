package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class BolsonaroGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        108F,11F,
        383F,8F,
        375F,167F,
        106F, 158F
    )
)