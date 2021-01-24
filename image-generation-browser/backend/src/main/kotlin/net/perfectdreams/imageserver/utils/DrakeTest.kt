package net.perfectdreams.imageserver.utils

import net.perfectdreams.imagegen.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.imagegen.generators.drake.LoriDrakeGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    println("Test")
    val samBadge =
            JVMImage(
                    ImageIO.read(
                            BolsoDrakeGenerator::class.java.getResourceAsStream("/image_templates/rip_tv/template.png")
                    )
            )

    val f =  File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\image-generators\\src\\jvmTest\\resources\\sources\\cat_passion.jpg")

    println(
            f.exists()
    )

    val catPassion =
            JVMImage(
                    ImageIO.read(
                            File("C:\\Users\\Leonardo\\Documents\\IdeaProjects\\GabrielaImageGen\\image-generators\\src\\jvmTest\\resources\\sources\\cat_passion.jpg")
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