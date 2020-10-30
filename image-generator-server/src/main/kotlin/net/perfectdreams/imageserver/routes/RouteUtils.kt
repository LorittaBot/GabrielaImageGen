package net.perfectdreams.imageserver.routes

object RouteUtils {
    /**
     * Converts the [input] to kebab-case
     *
     * @return the string converted to kebab case
     */
    fun convertToKebabCase(input: String): String {
        val x = input.removeSuffix("Generator")

        val newString = StringBuilder()

        for (index in x.indices) {
            val charAt = x[index]
            val nextChar = x.getOrNull(index + 1)

            if (charAt.isLowerCase() && nextChar?.isUpperCase() == true) {
                newString.append(charAt.toLowerCase())
                newString.append("-")
            } else {
                newString.append(charAt.toLowerCase())
            }
        }

        return newString.toString()
    }
}