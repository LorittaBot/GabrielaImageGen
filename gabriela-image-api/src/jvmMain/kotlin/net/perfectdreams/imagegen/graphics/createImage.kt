package net.perfectdreams.imagegen.graphics

import net.perfectdreams.imagegen.graphics.Image
import java.awt.image.BufferedImage

actual fun createImage(width: Int, height: Int, imageType: Image.ImageType): Image {
    return JVMImage(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))
}