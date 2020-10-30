package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class GessyAtaGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        130F, 35F,
        410F, 92F,
        387F, 277F,
        111F, 210F
    )
)