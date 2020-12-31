package net.perfectdreams.imageserver.utils

import net.perfectdreams.imagegen.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.imagegen.generators.drake.LoriDrakeGenerator
import net.perfectdreams.imagegen.generators.skewed.RipTvGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val ripTvTemplate =
        JVMImage(
            ImageIO.read(
                RipTvGenerator::class.java.getResourceAsStream("/image_templates/rip_tv/template.png")
            )
        )

    val catPassion =
        JVMImage(
            ImageIO.read(
                File("L:\\LorittaAssets\\GabrielaImageGen\\cat_passion.jpg")
            )
        )

    val samGenerator = RipTvGenerator(ripTvTemplate)

    File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\image-generators\\src\\jvmTest\\resources\\templates_check\\rip_tv.png")
        .writeBytes(
            samGenerator.generate(catPassion)
                .toByteArray(Image.FormatType.PNG)
        )
}