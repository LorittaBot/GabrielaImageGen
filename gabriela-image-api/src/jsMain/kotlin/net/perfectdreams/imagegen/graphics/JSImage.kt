package net.perfectdreams.imagegen.graphics

import net.perfectdreams.imagegen.utils.extensions.get2dContext
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

class JSImage(val canvas: HTMLCanvasElement) : Image {
    override val width: Int
        get() = canvas.width
    override val height: Int
        get() = canvas.height

    override fun getScaledInstance(width: Int, height: Int, scaleType: Image.ScaleType): Image {
        // Clonar imagem original
        val scaledImage = createCanvas(width, height)
        val ctx = scaledImage.getContext("2d") as CanvasRenderingContext2D
        ctx.drawImage(this.canvas.get2dContext().canvas, 0.0, 0.0, width.toDouble(), height.toDouble())

        println(scaledImage.width)
        println(scaledImage.height)

        return JSImage(scaledImage)
    }

    override fun getSkewedInstance(x0: Float, y0: Float, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Image {
        return JSImage(
            HackySkew(canvas)
                .setCorners(
                    x0, y0,
                    x1, y1,
                    x2, y2,
                    x3, y3
                )
        )
    }

    override fun createGraphics(): Graphics {
        return JSGraphics(canvas.get2dContext())
    }

    override fun toByteArray(type: Image.FormatType): ByteArray {
        val dataUrlFormat = when (type) {
            Image.FormatType.PNG -> "image/png"
            else -> throw IllegalArgumentException("Unsupported format $type")
        }

        val dataUrl = canvas.toDataURL(dataUrlFormat)
        val dataBase64 = dataUrl.split("base64,").last()
        val buf = Buffer.from(dataBase64, "base64")

        return buf.toByteArray()
    }

    /* override fun toByteArray(): ByteArray {
        val ctx = canvas.getContext("2d")
        val dataUrl = ctx.canvas.toDataURL("image/png")
        val dataBase64 = dataUrl.split("base64,").last()
        val buf = Buffer.from(dataBase64, "base64")

        return buf.toByteArray()
    } */
}