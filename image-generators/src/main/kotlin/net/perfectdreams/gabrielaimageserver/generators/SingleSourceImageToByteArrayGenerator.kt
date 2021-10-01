package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage

interface SingleSourceImageToByteArrayGenerator : Generator {
    fun generate(source: BufferedImage): ByteArray
}