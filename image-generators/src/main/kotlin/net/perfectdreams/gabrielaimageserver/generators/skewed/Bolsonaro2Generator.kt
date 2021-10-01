package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class Bolsonaro2Generator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        213F,34F,
        435F,40F,
        430F,166F,
        212F, 161F
    )
)