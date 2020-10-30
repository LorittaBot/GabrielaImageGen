package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class Bolsonaro2Generator(template: Image) : BasicSkewedImageGenerator(
    template,
    Corners(
        213F,34F,
        435F,40F,
        430F,166F,
        212F, 161F
    )
)