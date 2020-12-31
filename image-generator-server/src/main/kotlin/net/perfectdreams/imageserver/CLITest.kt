package net.perfectdreams.imageserver

import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoBandeiraGenerator
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import java.io.File
import java.lang.IllegalArgumentException
import javax.imageio.ImageIO

fun convertToSnakeCase(input: String): String {
    val x = input.removeSuffix("Test")
        .removeSuffix("Generator")
        .removeSuffix("CortesFlow")

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
    while (true) {
        val line = readLine()!!

        val (generatorType, filePath) = line.split(" ")

        val generatorPath = convertToSnakeCase(generatorType)

        val generator = Class.forName("net.perfectdreams.imagegen.generators.$generatorType")
            .getConstructor(Image::class.java)
            .newInstance(
                JVMImage(
                    ImageIO.read(
                        loadFromJar(EdnaldoBandeiraGenerator::class.java, "/$generatorPath/template.png")
                    )
                )
            )

        val image = if (generator is BasicScaledImageGenerator) {
             generator.generate(
                JVMImage(
                    ImageIO.read(
                        File(filePath)
                    )
                )
            )
        } else if (generator is BasicSkewedImageGenerator) {
            generator.generate(
                JVMImage(
                    ImageIO.read(
                        File(filePath)
                    )
                )
            )
        } else throw IllegalArgumentException("CLI doesn't support $generator")

        File("./$generatorPath.png")
            .writeBytes(image.toByteArray(Image.FormatType.PNG))

        println("Done!")
    }
}

fun loadFromJar(clazz: Class<*>, inputPath: String) = clazz.getResourceAsStream(inputPath)