package net.perfectdreams.gabrielaimageserver.generators.skewed

import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import java.awt.image.BufferedImage

class BobBurningPaperGenerator(template: BufferedImage) : BasicSkewedImageGenerator(
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