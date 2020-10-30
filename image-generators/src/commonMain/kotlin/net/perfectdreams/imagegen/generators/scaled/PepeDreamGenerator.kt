package net.perfectdreams.imagegen.generators.scaled

import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.graphics.Image

class PepeDreamGenerator(template: Image) : BasicScaledImageGenerator(
    template,
    100,
    100,
    81,
    2
)