package net.perfectdreams.gabrielaimageserver.generators.undertale.textbox

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class DeltaruneBoxGenerator(
    dialogBoxImage: BufferedImage,
    font: Font
) : TobyBoxGenerator(
    dialogBoxImage,
    font
) {
    override val colorsThatShouldBePresent = listOf(
        Color.BLACK,
        Color.WHITE,
        Color(3, 3, 125), // Text Shadow
        Color(157, 162, 196), // Gray Color of the Text Box
        Color(140, 206, 255) // Blue Dot of the Text Box
    )

    override fun drawString(graphics2D: Graphics2D, str: String, x: Int, y: Int) {
        graphics2D.color = Color(3, 3, 125) // Text Shadow
        graphics2D.drawString(
            str,
            x + 1,
            y + 1
        )

        graphics2D.color = Color.WHITE
        graphics2D.drawString(
            str,
            x,
            y
        )
    }
}