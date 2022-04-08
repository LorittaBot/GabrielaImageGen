package net.perfectdreams.gabrielaimageserver.generators.utils

import java.awt.AlphaComposite
import java.awt.Color
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage

object ImageUtils {
    /**
     * Enables font antialiasing for [graphics2D]
     *
     * @param graphics2D the [Graphics2D] instance that should have its text antialiasing rendering hints enabled
     * @return the [graphics2D] instance, useful for chaining
     */
    fun enableTextAntialiasing(graphics2D: Graphics2D): Graphics2D {
        val rh = RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        )
        graphics2D.setRenderingHints(rh)
        return graphics2D
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    fun toBufferedImage(img: Image, type: Int = BufferedImage.TYPE_INT_ARGB): BufferedImage {
        if (img is BufferedImage) {
            return img
        }

        // Create a buffered image with transparency
        val bimage = BufferedImage(img.getWidth(null), img.getHeight(null), type)

        // Draw the image on to the buffered image
        val bGr = bimage.createGraphics()
        bGr.drawImage(img, 0, 0, null)
        bGr.dispose()

        // Return the buffered image
        return bimage
    }

    /**
     * Draws a string with a outline around it, the text will be drawn with the current color set in the graphics object
     *
     * @param graphics     the image graphics
     * @param text         the text that will be drawn
     * @param x            where the text will be drawn in the x-axis
     * @param y            where the text will be drawn in the y-axis
     * @param outlineColor the color of the outline
     * @param power        the thickness of the outline
     */
    fun drawStringWithOutline(graphics: Graphics, text: String, x: Int, y: Int, outlineColor: Color = Color.BLACK, power: Int = 2) {
        val originalColor = graphics.color
        graphics.color = outlineColor
        for (powerX in -power..power) {
            for (powerY in -power..power) {
                graphics.drawString(text, x + powerX, y + powerY)
            }
        }

        graphics.color = originalColor
        graphics.drawString(text, x, y)
    }

    fun deepCopy(bi: BufferedImage): BufferedImage {
        val cm = bi.colorModel
        val isAlphaPremultiplied = cm.isAlphaPremultiplied
        val raster = bi.copyData(bi.raster.createCompatibleWritableRaster())
        return BufferedImage(cm, raster, isAlphaPremultiplied, null)
    }

    /**
     * Clones the [input] to a new [BufferedImage.TYPE_INT_ARGB] image
     *
     * This is useful to avoid issues (example: black backgrounds) when manipulating the source image
     */
    fun cloneAsARGB(input: Image): BufferedImage {
        val cloned = BufferedImage(input.getWidth(null), input.getHeight(null), BufferedImage.TYPE_INT_ARGB)
        val graphics = cloned.createGraphics()
        graphics.drawImage(input, 0, 0, null)
        graphics.dispose()
        return cloned
    }

    fun makeRoundedCorners(image: BufferedImage, cornerRadius: Int): BufferedImage {
        // https://stackoverflow.com/a/7603815/7271796
        val w = image.width
        val h = image.height
        val output = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

        val g2 = output.createGraphics()

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.composite = AlphaComposite.Src
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = Color.WHITE
        g2.fill(RoundRectangle2D.Float(0f, 0f, w.toFloat(), h.toFloat(), cornerRadius.toFloat(), cornerRadius.toFloat()))

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.composite = AlphaComposite.SrcIn // https://stackoverflow.com/a/32989394/7271796
        g2.drawImage(image, 0, 0, null)

        g2.dispose()

        return output
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param graphics The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    fun drawCenteredString(graphics: Graphics, text: String, rect: Rectangle) {
        // Get the FontMetrics
        val metrics = graphics.fontMetrics
        // Determine the X coordinate for the text
        val x = rect.x + (rect.width - metrics.stringWidth(text)) / 2
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        val y = rect.y + (rect.height - metrics.height) / 2 + metrics.ascent
        // Draw the String
        graphics.drawString(text, x, y)
    }

    /**
     * Escreve um texto em um Graphics, fazendo wrap caso necessário
     *
     * Esta versão separa entre espaços o texto, para ficar mais bonito
     *
     * @param text Texto
     * @param startX X inicial
     * @param startY Y inicial
     * @param endX X máximo, caso o texto ultrapasse o endX, ele automaticamente irá fazer wrap para a próxima linha
     * @param endY Y máximo, atualmente unused
     * @param fontMetrics Metrics da fonte
     * @param graphics Graphics usado para escrever a imagem
     * @return Y final
     */
    fun drawTextWrapSpaces(text: String, startX: Int, startY: Int, endX: Int, endY: Int, fontMetrics: FontMetrics, graphics: Graphics): Int {
        val lineHeight = fontMetrics.height // Aqui é a altura da nossa fonte

        var currentX = startX // X atual
        var currentY = startY // Y atual

        val split = text.split("((?<= )|(?= ))".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() // Nós precisamos deixar os espaços entre os splits!
        for (str in split) {
            var width = fontMetrics.stringWidth(str) // Width do texto que nós queremos colocar
            if (currentX + width > endX) { // Se o currentX é maior que o endX... (Nós usamos currentX + width para verificar "ahead of time")
                currentX = startX // Nós iremos fazer wrapping do texto
                currentY += lineHeight
            }
            var idx = 0
            for (c in str.toCharArray()) { // E agora nós iremos printar todos os chars
                idx++
                if (c == '\n') {
                    currentX = startX // Nós iremos fazer wrapping do texto
                    currentY += lineHeight
                    continue
                }
                width = fontMetrics.charWidth(c)
                /* if (!graphics.font.canDisplay(c)) {
                    // Talvez seja um emoji!
                    val emoteImage = getTwitterEmoji(str, idx)
                    if (emoteImage != null) {
                        graphics.drawImage(emoteImage.getScaledInstance(width, width, BufferedImage.SCALE_SMOOTH), currentX, currentY - width, null)
                        currentX += width
                    }

                    continue
                } */
                graphics.drawString(c.toString(), currentX, currentY) // Escreva o char na imagem
                currentX += width // E adicione o width no nosso currentX
            }
        }
        return currentY
    }
}