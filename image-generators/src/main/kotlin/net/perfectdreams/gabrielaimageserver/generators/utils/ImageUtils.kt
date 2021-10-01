package net.perfectdreams.gabrielaimageserver.generators.utils

import java.awt.AlphaComposite
import java.awt.Color
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
    fun toBufferedImage(img: Image): BufferedImage {
        if (img is BufferedImage) {
            return img
        }

        // Create a buffered image with transparency
        val bimage = BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)

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
}