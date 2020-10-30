package net.perfectdreams.imagegen.generators.skewed

import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Corners
import net.perfectdreams.imagegen.graphics.Image

class BobBurningPaperGenerator(template: Image) : BasicSkewedImageGenerator(
    template,
    listOf(
        Corners(
            21f, 373f,

            14f, 353f,

            48f, 334f,

            82f, 354f
        ),
        Corners(
            24f, 32f,

            138f, 33f,

            137f, 177f,

            20f, 175f
        )
    )
)