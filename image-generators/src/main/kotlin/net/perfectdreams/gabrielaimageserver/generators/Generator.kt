package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.toBufferedImage
import net.perfectdreams.gabrielaimageserver.graphics.Corners
import net.perfectdreams.gabrielaimageserver.graphics.LorittaImage
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage

interface Generator {
    /**
     * Creates an image with the specified [width], [height] and [type]
     *
     * This is a helper method for the BufferedImage constructor
     *
     * @see BufferedImage
     */
    fun createImage(width: Int, height: Int, type: Int = BufferedImage.TYPE_INT_ARGB)
            = BufferedImage(width, height, type)

    /**
     * Clones the image
     */
    fun BufferedImage.clone() = ImageUtils.deepCopy(this)

    /**
     * Scales the image and converts its result to a [BufferedImage]
     *
     * This is a helper method for [BufferedImage.getScaledInstance] and [toBufferedImage]
     *
     * @see BufferedImage
     * @see toBufferedImage
     */
    fun BufferedImage.getScaledBufferedImageInstance(width: Int, height: Int, hints: Int = BufferedImage.SCALE_SMOOTH)
            = this.getScaledInstance(width, height, hints).toBufferedImage()

    /**
     * Skews the [BufferedImage] and returns a new image with skew effect applied
     *
     * @see LorittaImage
     */
    fun BufferedImage.getSkewedInstance(corners: Corners) = getSkewedInstance(
        corners.upperLeftX, corners.upperLeftY,
        corners.upperRightX, corners.upperRightY,
        corners.lowerRightX, corners.lowerRightY,
        corners.lowerLeftX, corners.lowerLeftY
    )

    /**
     * Skews the [BufferedImage] and returns a new image with skew effect applied
     *
     * @see LorittaImage
     */
    fun BufferedImage.getSkewedInstance(
        upperLeftX: Float, upperLeftY: Float,
        upperRightX: Float, upperRightY: Float,
        lowerRightX: Float, lowerRightY: Float,
        lowerLeftX: Float, lowerLeftY: Float
    ) = LorittaImage(this).apply {
        setCorners(
            upperLeftX, upperLeftY,
            upperRightX, upperRightY,
            lowerRightX, lowerRightY,
            lowerLeftX, lowerLeftY
        )
    }.bufferedImage

    /**
     * Draws an image to the graphics canvas
     *
     * This is a helper method for [Graphics.drawImage], without the need to provide null as the observer argument
     *
     * @see BufferedImage
     */
    fun Graphics.drawImage(image: Image, x: Int, y: Int)
            = this.drawImage(image, x, y, null)
}