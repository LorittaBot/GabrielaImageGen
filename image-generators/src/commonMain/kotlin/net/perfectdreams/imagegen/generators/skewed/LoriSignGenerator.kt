package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class LoriSignGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        20f, 202f,
        155f, 226f,
        139f, 299f,
        3f, 275f
    )
)