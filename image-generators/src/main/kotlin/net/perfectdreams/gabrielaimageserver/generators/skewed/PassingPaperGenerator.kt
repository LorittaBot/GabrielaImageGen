package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class PassingPaperGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        220f, 210f,
        318f, 245f,
        266f, 335f,
        174f, 283f
    )
)