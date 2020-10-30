package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class LoriAtaGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        273F, 0F,
        768F, 0F,
        768F, 454F,
        245F, 354F
    )
)