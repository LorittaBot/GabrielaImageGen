package net.perfectdreams.imageserver.generators

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    File("L:\\LorittaAssets\\chaves\\overlay\\").listFiles().forEach {
        val image = createOptimizedOverlay(it)

        if (image != null) {
            ImageIO.write(
                    image,
                    "png",
                    File("L:\\LorittaAssets\\chaves\\optimized_overlays\\${it.name}")
            )
        }
    }

    File("L:\\LorittaAssets\\chaves\\optimized_overlays\\")
            .listFiles()
            .filter { it.extension == "png" }
            .forEach {
                println("Optimizing ${it.name}")

                ProcessBuilder(
                        "L:\\Tools\\pngquant\\pngquant.exe",
                        "--force",
                        "--verbose",
                        "--speed=1",
                        "--skip-if-larger",
                        "--strip",
                        "--quality",
                        "50-60",
                        it.path.toString(),
                        "--output",
                        it.path.toString()
                )
                        .redirectErrorStream(true)
                        .redirectOutput(
                                File("L:\\Tools\\pngquant\\kt-${it.name}.txt")
                        )

                        .start()
                        .waitFor()
                // System.exit(0)
            }
}

fun createOptimizedOverlay(file: File): BufferedImage? {
    val overlay = ImageIO.read(
            file
    )

    var firstNonTransparentPixelX = 9999
    var firstNonTransparentPixelY = 9999

    var lastNonTransparentPixelX = 0
    var lastNonTransparentPixelY = 0

    for (x in 0 until overlay.width) {
        for (y in 0 until overlay.height) {
            val pixelColor = Color(overlay.getRGB(x, y), true)

            if (pixelColor.alpha != 0) {
                if (firstNonTransparentPixelX > x)
                    firstNonTransparentPixelX = x

                if (firstNonTransparentPixelY > y)
                    firstNonTransparentPixelY = y

                if (x > lastNonTransparentPixelX)
                    lastNonTransparentPixelX = x
                if (y > lastNonTransparentPixelY)
                    lastNonTransparentPixelY = y
            }
        }
    }

    println("$firstNonTransparentPixelX; $firstNonTransparentPixelY")
    println("$lastNonTransparentPixelX; $lastNonTransparentPixelY")

    if (firstNonTransparentPixelX == 9999)
        return null

    val subimage = overlay.getSubimage(firstNonTransparentPixelX, firstNonTransparentPixelY, lastNonTransparentPixelX - firstNonTransparentPixelX, lastNonTransparentPixelY - firstNonTransparentPixelY)

    File("L:\\LorittaAssets\\chaves\\overlay_data.csv")
            .appendText("${file.name};$firstNonTransparentPixelX;$firstNonTransparentPixelY\n")
    return subimage

}