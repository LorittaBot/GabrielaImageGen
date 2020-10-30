package net.perfectdreams.imagegen.graphics

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException
import javax.imageio.ImageIO

class JVMImage(val handle: java.awt.Image) : Image {
	override val width: Int
		get() = handle.getWidth(null)
	override val height: Int
		get() = handle.getHeight(null)

	override fun getScaledInstance(width: Int, height: Int, scaleType: Image.ScaleType): JVMImage {
		val hints = when (scaleType) {
			Image.ScaleType.SMOOTH -> java.awt.Image.SCALE_SMOOTH
		}
		return JVMImage(handle.getScaledInstance(width, height, hints))
	}

	override fun getSkewedInstance(x0: Float, y0: Float, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Image {
		val image = LorittaImage(toBufferedImage(handle))

		image.setCorners(
				x0, y0,
				x1, y1,
				x2, y2,
				x3, y3
		)

		return JVMImage(image.bufferedImage)
	}

	override fun createGraphics(): Graphics {
		return JVMGraphics(handle.graphics)
	}

	override fun toByteArray(type: Image.FormatType): ByteArray {
		val output = object : ByteArrayOutputStream() {
			@Synchronized
			override fun toByteArray(): ByteArray {
				return this.buf
			}
		}

		val bufferedImage = if (handle !is BufferedImage)
			toBufferedImage(handle)
		else
			handle

		ImageIO.write(
			bufferedImage,
			when (type) {
				Image.FormatType.PNG -> "png"
				else -> throw IllegalArgumentException("Unsupported format $type")
			},
			output
		)

		return output.toByteArray()
	}

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	private fun toBufferedImage(img: java.awt.Image): BufferedImage {
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
}