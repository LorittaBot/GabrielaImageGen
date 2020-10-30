package net.perfectdreams.imageserver.utils

import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoBandeiraGenerator
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.generators.SAMGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.io.File
import java.lang.IllegalArgumentException
import javax.imageio.ImageIO

fun convertToSnakeCase(input: String): String {
    val x = input.removeSuffix("Test").removeSuffix("Generator")

    val newString = StringBuilder()

    for (index in x.indices) {
        val charAt = x[index]
        val nextChar = x.getOrNull(index + 1)

        if (charAt.isLowerCase() && nextChar?.isUpperCase() == true) {
            newString.append(charAt.toLowerCase())
            newString.append("_")
        } else {
            newString.append(charAt.toLowerCase())
        }
    }

    return newString.toString()
}

fun main() {
    val samBadge =
        JVMImage(
            ImageIO.read(
                SAMGenerator::class.java.getResourceAsStream("/sam/sam_1.png")
            )
        )

    val catPassion =
        JVMImage(
            ImageIO.read(
                File("C:\\Users\\Leonardo\\Documents\\LorittaAssets\\GabrielaImageGen\\cat_passion.jpg")
            )
        )

    val samGenerator = SAMGenerator(samBadge)

    File("generated_sam.png")
        .writeBytes(
            samGenerator.generate(catPassion)
                .toByteArray(Image.FormatType.PNG)
        )
}