package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class RomeroBrittoGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        16F,19F,
        201F,34F,
        208F,218F,
        52F, 294F
    )
)