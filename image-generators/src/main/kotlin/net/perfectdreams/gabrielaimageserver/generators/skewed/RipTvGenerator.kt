package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class RipTvGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        562F,65F, // UL
        721F,65F, // UR
        705F,178F, // LR
        557F, 170F // LL
    )
)