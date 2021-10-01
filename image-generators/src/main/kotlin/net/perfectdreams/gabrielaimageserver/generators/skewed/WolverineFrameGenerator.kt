package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class WolverineFrameGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        55F, 165F,
        152F, 159F,
        172F, 283F,
        73F, 293F
    )
)