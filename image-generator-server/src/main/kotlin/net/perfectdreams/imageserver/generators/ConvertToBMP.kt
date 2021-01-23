package net.perfectdreams.imageserver.generators

import java.io.File
import javax.imageio.ImageIO

fun main() {
    File("L:\\LorittaAssets\\chaves\\frames\\").listFiles().filter { it.extension == "jpeg" }.forEach {
        ImageIO.write(
                ImageIO.read(it),
                "bmp",
                File("L:\\LorittaAssets\\chaves\\frames_bmp\\${it.name.replace("jpeg", "bmp")}")
        )

    }
}