package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class EdnaldoTVGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        270f, 19f,
        411f, 21f,
        409f, 126f,
        272f, 128f
    )
)