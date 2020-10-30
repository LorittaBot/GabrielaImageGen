package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class ChicoAtaGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        300F, 0F,
        768F, 0F,
        768F, 480F,
        300F, 383F
    )
)