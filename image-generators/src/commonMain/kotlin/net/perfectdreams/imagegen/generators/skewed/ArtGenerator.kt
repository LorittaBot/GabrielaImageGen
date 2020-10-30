package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class ArtGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        75f, 215f,
        172f, 242f,
        106f, 399f,
        13f, 369f
    )
)