package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import net.perfectdreams.gabrielaimageserver.graphics.LorittaImage
import java.awt.BasicStroke
import java.awt.Color
import java.awt.RenderingHints
import java.awt.Stroke
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val background = BufferedImage(960 * 2, 960 * 2, BufferedImage.TYPE_INT_RGB)

    val graphics = background.createGraphics()
    graphics.color = Color(42, 176, 255)
    val halfWidth = background.width / 2
    val halfHeight = background.height / 2

    graphics.fillRect(0, 0, halfWidth, halfHeight)

    graphics.color = Color(15, 71, 236)
    graphics.fillRect(halfWidth, 0, halfWidth, halfHeight)

    graphics.color = Color(34, 126, 255)
    graphics.fillRect(halfWidth, halfHeight, halfWidth, halfHeight)

    graphics.color = Color(4, 0, 214)
    graphics.fillRect(0, halfHeight, halfWidth, halfHeight)

    var d = 0.0

    repeat(60) {
        val base = BufferedImage(960, 720, BufferedImage.TYPE_INT_RGB)
        val baseGraphics = base.createGraphics()
        baseGraphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        val rotatedBackground3 = LorittaImage(background).apply {
            rotate(d)
        }.bufferedImage

        val middleX = rotatedBackground3.width / 2
        val middleY = rotatedBackground3.height / 2

        baseGraphics.drawImage(
            rotatedBackground3,
            middleX - (rotatedBackground3.width) + (base.width / 2),
            middleY - (rotatedBackground3.height) + (base.height / 2),
            null
        )

        val rotatedBackground2 = LorittaImage(background).apply {
            rotate(360.0 - d)
        }.bufferedImage

        val circle2 = Ellipse2D.Float((base.width / 2) - 335f, (base.height / 2) - 335f, 335f + 335f, 335f + 335f)
        baseGraphics.clip = circle2

        baseGraphics.drawImage(
            rotatedBackground2,
            middleX - (rotatedBackground2.width) + (base.width / 2),
            middleY - (rotatedBackground2.height) + (base.height / 2),
            null
        )

        val circle3 = Ellipse2D.Float((base.width / 2) - 170f, (base.height / 2) - 170f, 170f + 170f, 170f + 170f)
        baseGraphics.clip = circle3

        baseGraphics.drawImage(
            rotatedBackground3,
            middleX - (rotatedBackground2.width) + (base.width / 2),
            middleY - (rotatedBackground2.height) + (base.height / 2),
            null
        )

        baseGraphics.clip = null
        baseGraphics.color = Color(108, 238, 239)
        baseGraphics.fillRect(0, (base.height / 2) - 7, base.width, 14)
        baseGraphics.fillRect((base.width / 2) - 7, 0, 14, base.height)

        val defaultStroke = baseGraphics.stroke

        baseGraphics.stroke = BasicStroke(14f)
        baseGraphics.drawOval(circle3.x.toInt(), circle3.y.toInt(), circle3.width.toInt(), circle3.height.toInt())
        baseGraphics.drawOval(circle2.x.toInt(), circle2.y.toInt(), circle2.width.toInt(), circle2.height.toInt())

        baseGraphics.stroke = defaultStroke

        ImageIO.write(
            base,
            "png",
            File("./chaves/opening$it.png")
        )

        ImageIO.write(
            rotatedBackground3,
            "png",
            File("./chaves_rotated/opening$it.png")
        )

        d += 2.25
    }
}