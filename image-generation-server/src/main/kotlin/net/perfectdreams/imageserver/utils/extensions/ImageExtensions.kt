package net.perfectdreams.imageserver.utils.extensions

import net.perfectdreams.imageserver.utils.ImageUtils
import java.awt.Image
import java.awt.image.BufferedImage

fun Image.toBufferedImage() : BufferedImage {
    return ImageUtils.toBufferedImage(this)
}
