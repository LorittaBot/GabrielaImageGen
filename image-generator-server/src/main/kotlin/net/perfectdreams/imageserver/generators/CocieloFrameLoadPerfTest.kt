package net.perfectdreams.imageserver.generators

import java.io.File
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

fun main() {
    val time = mutableListOf<Long>()

    repeat(1_000) {
        val t = measureTimeMillis {
            ImageIO.read(
                    File("L:\\LorittaAssets\\chaves\\chaves_000151.bmp")
            )
        }

        time += t
    }

    println("Took: ${time.average()}")
}