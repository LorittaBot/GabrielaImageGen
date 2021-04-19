package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image

interface SingleSourceImageGenerator {
    fun generate(source: Image): Image
}