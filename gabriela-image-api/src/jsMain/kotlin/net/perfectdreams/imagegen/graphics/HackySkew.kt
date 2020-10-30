package net.perfectdreams.imagegen.graphics

import org.khronos.webgl.get
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * Used to skew an image. Adapted from 2 image processing classes developed
 * by Jerry Huxtable (http://www.jhlabs.com) and released under
 * the Apache License, Version 2.0.
 *
 * Ported to Kotlin/JS by MrPowerGamerBR
 */
class HackySkew(private val canvas: HTMLCanvasElement) {
    data class Rectangle(
        var x: Int,
        var y: Int,
        var width: Int,
        var height: Int
    )

    protected var edgeAction = ZERO
    protected var interpolation = BILINEAR

    protected lateinit var transformedSpace: Rectangle
    protected lateinit var originalSpace: Rectangle

    private var x0: Float = 0.toFloat()
    private var y0: Float = 0.toFloat()
    private var x1: Float = 0.toFloat()
    private var y1: Float = 0.toFloat()
    private var x2: Float = 0.toFloat()
    private var y2: Float = 0.toFloat()
    private var x3: Float = 0.toFloat()
    private var y3: Float = 0.toFloat()
    private var dx1: Float = 0.toFloat()
    private var dy1: Float = 0.toFloat()
    private var dx2: Float = 0.toFloat()
    private var dy2: Float = 0.toFloat()
    private var dx3: Float = 0.toFloat()
    private var dy3: Float = 0.toFloat()
    private var A: Float = 0.toFloat()
    private var B: Float = 0.toFloat()
    private var C: Float = 0.toFloat()
    private var D: Float = 0.toFloat()
    private var E: Float = 0.toFloat()
    private var F: Float = 0.toFloat()
    private var G: Float = 0.toFloat()
    private var H: Float = 0.toFloat()
    private var I: Float = 0.toFloat()

    fun setCorners(x0: Float, y0: Float,
                   x1: Float, y1: Float,
                   x2: Float, y2: Float,
                   x3: Float, y3: Float): HTMLCanvasElement {
        this.x0 = x0
        this.y0 = y0
        this.x1 = x1
        this.y1 = y1
        this.x2 = x2
        this.y2 = y2
        this.x3 = x3
        this.y3 = y3

        dx1 = x1 - x2
        dy1 = y1 - y2
        dx2 = x3 - x2
        dy2 = y3 - y2
        dx3 = x0 - x1 + x2 - x3
        dy3 = y0 - y1 + y2 - y3

        println("dx1: $dx1")
        println("dy1: $dy1")
        println("dx2: $dx2")
        println("dy2: $dy2")
        println("dx3: $dx3")
        println("dy3: $dy3")

        val a11: Float
        val a12: Float
        val a13: Float
        val a21: Float
        val a22: Float
        val a23: Float
        val a31: Float
        val a32: Float

        if (dx3 == 0f && dy3 == 0f) {
            a11 = x1 - x0
            a21 = x2 - x1
            a31 = x0
            a12 = y1 - y0
            a22 = y2 - y1
            a32 = y0
            a23 = 0f
            a13 = a23
        } else {
            a13 = (dx3 * dy2 - dx2 * dy3) / (dx1 * dy2 - dy1 * dx2)
            a23 = (dx1 * dy3 - dy1 * dx3) / (dx1 * dy2 - dy1 * dx2)
            a11 = x1 - x0 + a13 * x1
            a21 = x3 - x0 + a23 * x3
            a31 = x0
            a12 = y1 - y0 + a13 * y1
            a22 = y3 - y0 + a23 * y3
            a32 = y0
        }

        A = a22 - a32 * a23
        B = a31 * a23 - a21
        C = a21 * a32 - a31 * a22
        D = a32 * a13 - a12
        E = a11 - a31 * a13
        F = a31 * a12 - a11 * a32
        G = a12 * a23 - a22 * a13
        H = a21 * a13 - a11 * a23
        I = a11 * a22 - a21 * a12

        println("A: $A")
        println("B: $B")
        println("C: $C")
        println("D: $D")
        println("E: $E")
        println("F: $F")
        println("G: $G")
        println("H: $H")
        println("I: $I")

        return filter(canvas, createCanvas(canvas.width, canvas.height))
    }

    protected fun transformSpace(rect: Rectangle) {
        rect.x = min(min(x0, x1), min(x2, x3)).toInt()
        rect.y = min(min(y0, y1), min(y2, y3)).toInt()
        rect.width = max(max(x0, x1), max(x2, x3)).toInt() - rect.x
        rect.height = max(max(y0, y1), max(y2, y3)).toInt() - rect.y
    }

    private fun filter(src: HTMLCanvasElement, dst: HTMLCanvasElement): HTMLCanvasElement {
        val width = src.width
        val height = src.height
        console.log("width is $width, height is $height. Interpolation type is ${interpolation} Edge Action is ${edgeAction}")

        originalSpace = Rectangle(0, 0, width, height)
        transformedSpace = Rectangle(0, 0, width, height)
        console.log("teh og: $originalSpace")
        transformSpace(transformedSpace)
        console.log("after transform: $transformedSpace")

        console.log("gettin them pixels uwu")
        val inPixels = getRGB(src, 0, 0, width, height)
        console.log("there are ${inPixels.size} pixels, very owo")

        if (interpolation == NEAREST_NEIGHBOUR)
            return filterPixelsNN(dst, width, height, inPixels, transformedSpace)

        val srcWidth1 = width - 1
        val srcHeight1 = height - 1
        val outWidth = transformedSpace.width
        val outHeight = transformedSpace.height
        val outX: Int
        val outY: Int
        //int index = 0;
        val outPixels = IntArray(outWidth)

        outX = transformedSpace.x
        outY = transformedSpace.y
        val out = FloatArray(2)

        for (y in 0 until outHeight) {
            for (x in 0 until outWidth) {
                transformInverse(outX + x, outY + y, out)
                val srcX = floor(out[0].toDouble()).toInt()
                val srcY = floor(out[1].toDouble()).toInt()
                val xWeight = out[0] - srcX
                val yWeight = out[1] - srcY
                val nw: Int
                val ne: Int
                val sw: Int
                val se: Int

                if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {
                    // Easy case, all corners are in the image
                    val i = width * srcY + srcX
                    nw = inPixels[i]
                    ne = inPixels[i + 1]
                    sw = inPixels[i + width]
                    se = inPixels[i + width + 1]
                } else {
                    // Some of the corners are off the image
                    nw = getPixel(inPixels, srcX, srcY, width, height)
                    ne = getPixel(inPixels, srcX + 1, srcY, width, height)
                    sw = getPixel(inPixels, srcX, srcY + 1, width, height)
                    se = getPixel(inPixels, srcX + 1, srcY + 1, width, height)
                }
                outPixels[x] = bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se)
            }
            setRGB(dst, outX, outY + y, transformedSpace.width, 1, outPixels)
        }
        return dst
    }

    private fun getPixel(pixels: IntArray, x: Int, y: Int, width: Int, height: Int): Int {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            when (edgeAction) {
                ZERO -> return 0
                WRAP -> return pixels[mod(y, height) * width + mod(x, width)]
                CLAMP -> return pixels[clamp(y, 0, height - 1) * width + clamp(x, 0, width - 1)]
                else -> return 0
            }
        }
        return pixels[y * width + x]
    }


    protected fun filterPixelsNN(dst: HTMLCanvasElement, width: Int,
                                 height: Int, inPixels: IntArray, transformedSpace: Rectangle
    ): HTMLCanvasElement {
        val outWidth = transformedSpace.width
        val outHeight = transformedSpace.height
        val outX: Int
        val outY: Int
        var srcX: Int
        var srcY: Int
        val outPixels = IntArray(outWidth)

        outX = transformedSpace.x
        outY = transformedSpace.y
        val rgb = IntArray(4)
        val out = FloatArray(2)

        for (y in 0 until outHeight) {
            for (x in 0 until outWidth) {
                transformInverse(outX + x, outY + y, out)
                srcX = out[0].toInt()
                srcY = out[1].toInt()
                // int casting rounds towards zero, so we check out[0] < 0, not srcX < 0
                if (out[0] < 0 || srcX >= width || out[1] < 0 || srcY >= height) {
                    val p: Int
                    when (edgeAction) {
                        ZERO -> p = 0
                        WRAP -> p = inPixels[mod(srcY, height) * width + mod(srcX, width)]
                        CLAMP -> p = inPixels[clamp(srcY, 0, height - 1) * width + clamp(srcX, 0, width - 1)]
                        else -> p = 0
                    }
                    outPixels[x] = p
                } else {
                    val i = width * srcY + srcX
                    rgb[0] = inPixels[i]
                    outPixels[x] = inPixels[i]
                }
            }
            setRGB(dst, 0, y, transformedSpace.width, 1, outPixels)
        }
        return dst
    }


    protected fun transformInverse(x: Int, y: Int, out: FloatArray) {
        out[0] = originalSpace.width * (A * x + B * y + C) / (G * x + H * y + I)
        out[1] = originalSpace.height * (D * x + E * y + F) / (G * x + H * y + I)
    }

    /*
public Rectangle2D getBounds2D( BufferedImage src ) {
    return new Rectangle(0, 0, src.getWidth(), src.getHeight());
}

public Point2D getPoint2D( Point2D srcPt, Point2D dstPt ) {
    if ( dstPt == null )
        dstPt = new Point2D.Double();
    dstPt.setLocation( srcPt.getX(), srcPt.getY() );
    return dstPt;
}
*/

    /**
     * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance
     * penalty of BufferedImage.getRGB unmanaging the image.
     */
    fun getRGB(image: HTMLCanvasElement, x: Int, y: Int, width: Int, height: Int): IntArray {
        // console.log("Getting RGB at $x, $y width: $width height: $height")
        val imageData = (image.getContext("2d") as CanvasRenderingContext2D).getImageData(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
        val intList = mutableListOf<Int>()
        val data = imageData.data

        for (i in 0 until imageData.data.length step 4) {
            val r = data[i].toInt()
            val g = data[i + 1].toInt()
            val b = data[i + 2].toInt()
            val a = data[i + 3].toInt()
            // let's pack!
            val packedRGB = (a shl 24) + (r shl 16) + (g shl 8) + b
            intList.add(packedRGB)
        }

        return intList.toIntArray()
    }

    /**
     * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance
     * penalty of BufferedImage.setRGB unmanaging the image.
     */
    fun setRGB(image: HTMLCanvasElement, x: Int, y: Int, width: Int, height: Int, pixels: IntArray) {
        // console.log("Setting RGB at $x, $y $width $height There are ${pixels.size} array pixels")
        val ctx = image.getContext("2d") as CanvasRenderingContext2D
        var xCoordinate = x
        var yCoordinate = y

        for (packedRGB in pixels) {
            val a = packedRGB shr 24 and 0x000000FF
            val r = packedRGB shr 16 and 0x000000FF
            val g = packedRGB shr 8 and 0x000000FF
            val b = packedRGB and 0x000000FF

            // console.log("rgba($r, $g, $b, ${a / 255})")

            ctx.fillStyle = "rgba($r, $g, $b, ${a / 255})"
            ctx.fillRect(xCoordinate.toDouble(), yCoordinate.toDouble(), 1.toDouble(), 1.toDouble())
            xCoordinate++

            if (xCoordinate >= canvas.width) {
                xCoordinate = x
                yCoordinate++
            }
        }
    }

    /**
     * Clamp a value to an interval.
     * @param a the lower clamp threshold
     * @param b the upper clamp threshold
     * @param x the input parameter
     * @return the clamped value
     */
    private fun clamp(x: Float, a: Float, b: Float): Float {
        return if (x < a) a else if (x > b) b else x
    }

    /**
     * Clamp a value to an interval.
     * @param a the lower clamp threshold
     * @param b the upper clamp threshold
     * @param x the input parameter
     * @return the clamped value
     */
    private fun clamp(x: Int, a: Int, b: Int): Int {
        return if (x < a) a else if (x > b) b else x
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    private fun mod(a: Double, b: Double): Double {
        var a = a
        val n = (a / b).toInt()

        a -= n * b
        return if (a < 0) a + b else a
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    private fun mod(a: Float, b: Float): Float {
        var a = a
        val n = (a / b).toInt()

        a -= n * b
        return if (a < 0) a + b else a
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    private fun mod(a: Int, b: Int): Int {
        var a = a
        val n = a / b

        a -= n * b
        return if (a < 0) a + b else a
    }


    /**
     * Bilinear interpolation of ARGB values.
     * @param x the X interpolation parameter 0..1
     * @param y the y interpolation parameter 0..1
     * @return the interpolated value
     */
    private fun bilinearInterpolate(x: Float, y: Float, nw: Int, ne: Int, sw: Int, se: Int): Int {
        var m0: Float
        var m1: Float
        val a0 = nw shr 24 and 0xff
        val r0 = nw shr 16 and 0xff
        val g0 = nw shr 8 and 0xff
        val b0 = nw and 0xff
        val a1 = ne shr 24 and 0xff
        val r1 = ne shr 16 and 0xff
        val g1 = ne shr 8 and 0xff
        val b1 = ne and 0xff
        val a2 = sw shr 24 and 0xff
        val r2 = sw shr 16 and 0xff
        val g2 = sw shr 8 and 0xff
        val b2 = sw and 0xff
        val a3 = se shr 24 and 0xff
        val r3 = se shr 16 and 0xff
        val g3 = se shr 8 and 0xff
        val b3 = se and 0xff

        val cx = 1.0f - x
        val cy = 1.0f - y

        m0 = cx * a0 + x * a1
        m1 = cx * a2 + x * a3
        val a = (cy * m0 + y * m1).toInt()

        m0 = cx * r0 + x * r1
        m1 = cx * r2 + x * r3
        val r = (cy * m0 + y * m1).toInt()

        m0 = cx * g0 + x * g1
        m1 = cx * g2 + x * g3
        val g = (cy * m0 + y * m1).toInt()

        m0 = cx * b0 + x * b1
        m1 = cx * b2 + x * b3
        val b = (cy * m0 + y * m1).toInt()

        return a shl 24 or (r shl 16) or (g shl 8) or b
    }

    companion object {

        val ZERO = 0
        val CLAMP = 1
        val WRAP = 2

        val NEAREST_NEIGHBOUR = 0
        val BILINEAR = 1
    }


} //end skew class