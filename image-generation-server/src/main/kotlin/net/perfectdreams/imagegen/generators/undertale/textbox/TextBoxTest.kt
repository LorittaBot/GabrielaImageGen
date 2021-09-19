package net.perfectdreams.imagegen.generators.undertale.textbox

import java.awt.Font
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

fun main() {
    val utBoxGenerator = UndertaleBoxGenerator(
        ImageIO.read(
            File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\image_templates\\undertale\\textbox\\original.png"),
        ),
        Font.createFont(Font.TRUETYPE_FONT, File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\fonts\\dtm-mono.otf"))
    )

    val millis = measureTimeMillis {
        File("L:\\CinnamonAssets\\ImageTests\\dark_world_box.gif")
            .writeBytes(
                utBoxGenerator.generate(
                    "I thought you might want to keep it here in case you want to read it!",
                    CharacterPortrait.fromGame(
                        ImageIO.read(
                            File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\image_templates\\undertale\\textbox\\characters\\ralsei_with_hat\\angry.png")
                        )
                    )
                )
            )
    }

    println("Finished: ${millis}ms")
}