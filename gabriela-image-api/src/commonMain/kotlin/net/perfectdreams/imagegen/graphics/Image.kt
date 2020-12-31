package net.perfectdreams.imagegen.graphics

interface Image {
    enum class ScaleType {
        SMOOTH
    }

    enum class ImageType {
        ARGB
    }

    enum class FormatType {
        PNG,
        JPEG
    }

    val width: Int
    val height: Int

    fun getScaledInstance(width: Int, height: Int, scaleType: ScaleType): Image
    fun getSkewedInstance(
        x0: Float, y0: Float, // UL
        x1: Float, y1: Float, // UR
        x2: Float, y2: Float, // LR
        x3: Float, y3: Float): Image // LL

    fun createGraphics(): Graphics

    fun toByteArray(type: FormatType): ByteArray

    fun clone(): Image {
        val image = createImage(width, height)
        val graphics = image.createGraphics()
        graphics.drawImage(this, 0, 0)
        return image
    }
}