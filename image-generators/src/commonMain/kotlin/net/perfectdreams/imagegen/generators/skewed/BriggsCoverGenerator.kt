package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class BriggsCoverGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        242F,67F, // UL
        381F,88F, // UR
        366F,266F, // LR
        218F, 248F // LL
    )
)