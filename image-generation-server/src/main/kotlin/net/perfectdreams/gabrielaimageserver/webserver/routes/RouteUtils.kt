package net.perfectdreams.gabrielaimageserver.webserver.routes

object RouteUtils {
    /**
     * Converts the [input] to kebab-case
     *
     * @return the string converted to kebab case
     */
    fun convertToKebabCase(input: String): String {
        val x = input.removeSuffix("Generator")
            .removeSuffix("CortesFlow")

        val newString = StringBuilder()

        for (index in x.indices) {
            val charAt = x[index]
            val nextChar = x.getOrNull(index + 1)

            if (charAt.isLowerCase() && nextChar?.isUpperCase() == true) {
                newString.append(charAt.lowercaseChar())
                newString.append("-")
            } else {
                newString.append(charAt.lowercaseChar())
            }
        }

        return newString.toString()
    }
}