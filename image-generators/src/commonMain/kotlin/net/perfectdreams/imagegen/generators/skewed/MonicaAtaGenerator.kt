package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class MonicaAtaGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        107F, 0F,
        300F, 0F,
        300F, 177F,
        96F, 138F
    )
)