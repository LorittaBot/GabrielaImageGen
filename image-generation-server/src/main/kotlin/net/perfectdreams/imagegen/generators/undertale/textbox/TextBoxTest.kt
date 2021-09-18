package net.perfectdreams.imagegen.generators.undertale.textbox

import java.awt.Font
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val utBoxGenerator = UndertaleBoxGenerator(
        ImageIO.read(
            File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\image_templates\\undertale\\textbox\\original.png"),
        ),
        Font.createFont(Font.TRUETYPE_FONT, File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\fonts\\dtm-mono.otf"))
    )

    File("L:\\CinnamonAssets\\ImageTests\\dark_world_box.gif")
        .writeBytes(
            utBoxGenerator.generate(
                "vai tomar no fiofo",
                CharacterPortrait.fromGame(
                    ImageIO.read(
                        File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\image_templates\\undertale\\textbox\\characters\\ralsei_with_hat\\angry.png")
                    )
                )
            )
        )
}