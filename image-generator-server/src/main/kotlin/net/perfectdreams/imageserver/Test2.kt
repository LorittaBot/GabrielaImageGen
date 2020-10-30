package net.perfectdreams.imageserver

import net.perfectdreams.imagegen.generators.skewed.EdnaldoTVGenerator
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

fun main() {
    ImageIO.setUseCache(true)

    val x = measureTimeMillis {
        repeat(1_000) {
            val image = ImageIO.read(
                loadFromJar(EdnaldoTVGenerator::class.java, "/ednaldo_tv/template.png")
            )
        }
    }

    println(x)
}