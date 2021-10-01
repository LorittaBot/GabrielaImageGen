package net.perfectdreams.gabrielaimageserver.generators.scaled

import net.perfectdreams.gabrielaimageserver.generators.BasicScaledImageGenerator
import java.awt.image.BufferedImage

class PepeDreamGenerator(template: BufferedImage) : BasicScaledImageGenerator(
    template,
    100,
    100,
    81,
    2
)