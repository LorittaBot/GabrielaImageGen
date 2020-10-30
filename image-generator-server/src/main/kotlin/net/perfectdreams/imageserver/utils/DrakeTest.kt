package net.perfectdreams.imageserver.utils

import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoBandeiraGenerator
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.generators.SAMGenerator
import net.perfectdreams.imagegen.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.imagegen.generators.drake.DrakeGenerator
import net.perfectdreams.imagegen.generators.drake.LoriDrakeGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.io.File
import java.lang.IllegalArgumentException
import javax.imageio.ImageIO

fun main() {
    val samBadge =
        JVMImage(
            ImageIO.read(
                BolsoDrakeGenerator::class.java.getResourceAsStream("/lori_drake/template.png")
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