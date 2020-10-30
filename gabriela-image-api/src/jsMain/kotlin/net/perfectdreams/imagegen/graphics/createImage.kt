package net.perfectdreams.imagegen.graphics

actual fun createImage(width: Int, height: Int, imageType: Image.ImageType): Image {
    return JSImage(createCanvas(width, height))
}