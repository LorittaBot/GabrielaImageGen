package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class EdnaldoBandeiraGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        44f, 220f,
        480f, 174f,
        340f, 516f,
        55f, 498f
    )
)