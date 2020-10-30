package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import kotlin.math.max
import kotlin.random.Random

class SAMGenerator(val samLogo: Image) {
    fun generate(source: Image): Image {
        val random = Random(10)
        val x = random.nextInt(0, max(1, source.width - samLogo.width))
        val y = random.nextInt(0, max(1, source.height - samLogo.height))

        return generate(source, x, y)
    }

    fun generate(source: Image, x: Int, y: Int): Image {
        val div = 2

        val height = (source.height / div).toInt() // We are going to scale based on the source's height
        // originalHeight -- newHeight
        // originalWidth -- newWidth
        val width = ((samLogo.width * height) / samLogo.height) // And now scale the width accordingly

        val seloSouthAmericaMemes = samLogo.getScaledInstance(width, height, Image.ScaleType.SMOOTH)

        source.createGraphics().drawImage(seloSouthAmericaMemes, x, y)

        return source
    }
}