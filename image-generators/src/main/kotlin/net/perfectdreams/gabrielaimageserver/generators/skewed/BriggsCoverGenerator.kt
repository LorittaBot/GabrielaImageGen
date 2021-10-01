package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class BriggsCoverGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
    template,
    Corners(
        242F,67F, // UL
        381F,88F, // UR
        366F,266F, // LR
        218F, 248F // LL
    )
)