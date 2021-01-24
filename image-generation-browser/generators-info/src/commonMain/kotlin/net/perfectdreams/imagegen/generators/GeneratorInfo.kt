package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image

class GeneratorInfo(
    val name: String,
    val path: String,
    val generator: (Image) -> (BasicSkewedImageGenerator)
) {
    val urlPath = path.replace("_", "-")
}