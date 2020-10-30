package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class WolverineFrameGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        55F, 165F,
        152F, 159F,
        172F, 283F,
        73F, 293F
    )
)