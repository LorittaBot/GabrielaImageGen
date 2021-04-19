package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.awt.image.BufferedImage

interface SingleSourceBufferedImageGenerator : SingleSourceImageGenerator {
    // Well let's hope that the backed image is always a BufferedImage
    override fun generate(source: Image) = generate((source as JVMImage).handle as BufferedImage)

    fun generate(source: BufferedImage): Image
}