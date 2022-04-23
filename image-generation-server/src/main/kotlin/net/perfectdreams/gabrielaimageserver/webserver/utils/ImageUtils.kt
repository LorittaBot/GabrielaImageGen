package net.perfectdreams.gabrielaimageserver.webserver.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.exceptions.ContentLengthTooLargeException
import net.perfectdreams.gabrielaimageserver.exceptions.ImageTooLargeException
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageFormatType
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

object ImageUtils {
    private val logger = KotlinLogging.logger {}
    private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0"

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

    /**
     * Downloads an image and returns it as a BufferedImage, additional checks are made and can be customized to avoid
     * downloading unsafe/big images that crash the application.
     *
     * @param url                            the image URL
     * @param connectTimeout                 the connection timeout
     * @param readTimeout                    the read timeout
     * @param maxSize                        the image's maximum size
     * @param overrideTimeoutsForSafeDomains if the URL is a safe domain, ignore timeouts
     * @param maxWidth                       the image's max width
     * @param maxHeight                      the image's max height
     * @param bypassSafety                   if the safety checks should be bypassed
     *
     * @return the image as a BufferedImage or null, if the image is considered unsafe
     */
    @JvmOverloads
    suspend fun downloadImage(url: String, connectTimeout: Int = 10, readTimeout: Int = 60, maxSize: Int = 8_388_608 /* 8mib */, overrideTimeoutsForSafeDomains: Boolean = false, maxWidth: Int = 2_500, maxHeight: Int = 2_500, bypassSafety: Boolean = false): BufferedImage? {
        val imageUrl = URL(url)
        val connection = imageUrl.openConnection() as HttpURLConnection

        connection.setRequestProperty(
                "User-Agent",
                USER_AGENT
        )

        val contentLength = connection.getHeaderFieldInt("Content-Length", 0)

        if (contentLength > maxSize) {
            logger.warn { "Image $url exceeds the maximum allowed Content-Length! ${connection.getHeaderFieldInt("Content-Length", 0)} > $maxSize"}
            throw ContentLengthTooLargeException()
        }

        if (connectTimeout != -1) {
            connection.connectTimeout = connectTimeout
        }

        if (readTimeout != -1) {
            connection.readTimeout = readTimeout
        }

        logger.debug { "Reading image $url; connectTimeout = $connectTimeout; readTimeout = $readTimeout; maxSize = $maxSize bytes; overrideTimeoutsForSafeDomains = $overrideTimeoutsForSafeDomains; maxWidth = $maxWidth; maxHeight = $maxHeight"}

        val imageBytes = withContext(Dispatchers.IO) {
            if (contentLength != 0) {
                // If the Content-Length is known (example: images on Discord's CDN do have Content-Length on the response header)
                // we can allocate the array with exactly the same size that the Content-Length provides, this way we avoid a lot of unnecessary Arrays.copyOf!
                // Of course, this could be abused to allocate a gigantic array that causes Loritta to crash, but if the Content-Length is present, Loritta checks the size
                // before trying to download it, so no worries :)
                connection.inputStream.readAllBytes(maxSize, contentLength)
            } else
                connection.inputStream.readAllBytes(maxSize)
        }

        val imageInfo = SimpleImageInfo(imageBytes)

        logger.debug { "Image $url was successfully downloaded! width = ${imageInfo.width}; height = ${imageInfo.height}; mimeType = ${imageInfo.mimeType}"}

        if (imageInfo.width > maxWidth || imageInfo.height > maxHeight) {
            logger.warn { "Image $url exceeds the maximum allowed width/height! ${imageInfo.width} > $maxWidth; ${imageInfo.height} > $maxHeight"}
            throw ImageTooLargeException()
        }

        return ImageIO.read(imageBytes.inputStream())
    }

    suspend fun BufferedImage.toByteArray(formatType: ImageFormatType) = withContext(Dispatchers.IO) {
        val output = ByteArrayOutputStream()
        ImageIO.write(this@toByteArray, formatType.name, output)
        output.toByteArray()
    }
}