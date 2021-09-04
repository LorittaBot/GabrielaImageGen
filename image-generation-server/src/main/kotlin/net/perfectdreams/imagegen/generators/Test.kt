package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import java.awt.Font
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

fun main() {
    val sticker = ImageIO.read(File("L:\\Pictures\\nosa_sticker.png"))
    val font = Font.createFont(Font.TRUETYPE_FONT, File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\assets\\fonts\\gorditas-bold.ttf"))

    val assets = ShipGenerator.ShipGeneratorAssets(
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\base_sparkling_hearts.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\base_hearts.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\base_shrug.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\base_sob.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\base_skulls.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\base_outline.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\status_sparkling_heart.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\status_heart.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\status_shrug.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\status_sob.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\status_skull.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\avatar_wrapper_outline.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\avatar_wrapper_background.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\progress_bar.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\progress_bar_background.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\progress_bar_overlay.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\progress_bar_reflection.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\gabi_resized.png")),
        ImageIO.read(File("L:\\RandomProjects\\BetterShip\\layers\\pantufa_resized.png")),
        font
    )

    val time = measureTimeMillis {
        repeat(101) {
            val image = ShipGenerator(assets).generate(
                sticker,
                sticker,
                it
            )

            File("L:\\RandomProjects\\BetterShip\\results\\$it.png")
                .writeBytes(
                    image.toByteArray(Image.FormatType.PNG)
                )
        }
    }

    println("Took ${time}ms")
}