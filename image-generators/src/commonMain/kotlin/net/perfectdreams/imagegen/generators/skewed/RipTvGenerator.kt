package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class RipTvGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        562F,65F, // UL
        721F,65F, // UR
        705F,178F, // LR
        557F, 170F // LL
    )
)