package net.perfectdreams.gabrielaimageserver.generators.utils

import java.io.File
import java.util.*

/**
 * Random functions useful for generators
 */
object GeneratorsUtils {
    fun generateFileName(name: String, extension: String) =
        "$name-${System.currentTimeMillis()}-${UUID.randomUUID()}.$extension"

    fun readBytesAndDelete(file: File): ByteArray {
        val bytes = file.readBytes()
        file.delete()
        return bytes
    }

    /**
     * Converts the [input] class name to snake_case, also removes the suffixes "Test", "CortesFlow" and "Generator"
     *
     * @param input the class name that will be converted to snake_case
     * @return the class name in snake_case
     */
    fun convertToSnakeCase(input: String): String {
        val x = input.removeSuffix("Test").removeSuffix("Generator").removeSuffix("CortesFlow")

        val newString = StringBuilder()

        for (index in x.indices) {
            val charAt = x[index]
            val nextChar = x.getOrNull(index + 1)

            if (charAt.isLowerCase() && nextChar?.isUpperCase() == true) {
                newString.append(charAt.lowercaseChar())
                newString.append("_")
            } else {
                newString.append(charAt.lowercaseChar())
            }
        }

        return newString.toString()
    }
}