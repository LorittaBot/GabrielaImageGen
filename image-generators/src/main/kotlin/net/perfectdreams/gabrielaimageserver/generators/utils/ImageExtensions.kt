package net.perfectdreams.gabrielaimageserver.generators.utils

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage

fun Image.toBufferedImage() : BufferedImage {
    return ImageUtils.toBufferedImage(this)
}

fun Graphics2D.enableFontAntialiasing(): Graphics2D {
    this.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    )
    return this
}

fun Graphics.drawStringWithOutline(text: String, x: Int, y: Int, outlineColor: Color = Color.BLACK, power: Int = 2)
        = ImageUtils.drawStringWithOutline(this, text, x, y, outlineColor, power)