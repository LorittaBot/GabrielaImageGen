package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class BolsonaroGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        108F,11F,
        383F,8F,
        375F,167F,
        106F, 158F
    )
)