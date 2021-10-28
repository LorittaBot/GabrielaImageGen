package net.perfectdreams.gabrielaimageserver.generators.scaled

import net.perfectdreams.gabrielaimageserver.generators.BasicScaledImageGenerator
import java.awt.image.BufferedImage

class MarkMetaGenerator(template: BufferedImage) : BasicScaledImageGenerator(
    template,
    411,
    300,
    513,
    88
)