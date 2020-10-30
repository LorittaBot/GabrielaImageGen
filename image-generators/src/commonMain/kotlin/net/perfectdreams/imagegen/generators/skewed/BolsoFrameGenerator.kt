package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class BolsoFrameGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        314F, 36F,
        394F, 41F,
        385F, 156F,
        301F, 151F
    )
)