package net.perfectdreams.imageserver.utils

import net.perfectdreams.imagegen.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.imagegen.generators.drake.LoriDrakeGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val samBadge =
        JVMImage(
            ImageIO.read(
                BolsoDrakeGenerator::class.java.getResourceAsStream("/image_templates/lori_drake/template.png")
            )
        )

    val catPassion =
        JVMImage(
            ImageIO.read(
                File("C:\\Users\\Leonardo\\Documents\\LorittaAssets\\GabrielaImageGen\\cat_passion.jpg")
            )
        )

    val samGenerator = LoriDrakeGenerator(samBadge)

    File("generated_sam.png")
        .writeBytes(
            samGenerator.generate(catPassion, catPassion)
                .toByteArray(Image.FormatType.PNG)
        )

    File("generated_sam2.png")
        .writeBytes(
            samGenerator.generate(catPassion, catPassion)
                .toByteArray(Image.FormatType.PNG)
        )
}