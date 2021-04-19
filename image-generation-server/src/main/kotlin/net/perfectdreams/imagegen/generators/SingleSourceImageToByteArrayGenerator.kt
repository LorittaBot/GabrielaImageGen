package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image

interface SingleSourceImageToByteArrayGenerator {
    fun generate(source: Image): ByteArray
}