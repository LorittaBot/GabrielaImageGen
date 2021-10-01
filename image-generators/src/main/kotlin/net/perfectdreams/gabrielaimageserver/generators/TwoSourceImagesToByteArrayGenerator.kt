package net.perfectdreams.gabrielaimageserver.generators

import java.awt.image.BufferedImage

interface TwoSourceImagesToByteArrayGenerator : Generator {
    fun generate(source1: BufferedImage, source2: BufferedImage): ByteArray
}