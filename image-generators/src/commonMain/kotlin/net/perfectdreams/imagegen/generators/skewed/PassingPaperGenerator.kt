package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class PassingPaperGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        220f, 210f,
        318f, 245f,
        266f, 335f,
        174f, 283f
    )
)