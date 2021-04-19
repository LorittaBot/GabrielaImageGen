package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image

interface TwoSourceImagesGenerator {
    fun generate(source1: Image, source2: Image): Image
}