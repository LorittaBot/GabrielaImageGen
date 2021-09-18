package net.perfectdreams.imagegen.generators.undertale.textbox

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class UndertaleBoxGenerator(
    dialogBoxImage: BufferedImage,
    font: Font
) : TobyBoxGenerator(dialogBoxImage, font) {
    override fun drawString(graphics2D: Graphics2D, str: String, x: Int, y: Int) {
        graphics2D.color = Color.WHITE
        graphics2D.drawString(
            str,
            x,
            y
        )
    }
}