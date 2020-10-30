import java.awt.image.BufferedImage

fun loadFromJar(clazz: Class<*>, inputPath: String) = clazz
    .getResourceAsStream(inputPath)

fun checkIfImagesAreEqual(image1: BufferedImage, image2: BufferedImage) {
    assert(image1.width == image2.width) { "Image dimensions doesn't match! image1.width: ${image1.width}; image2.width: ${image2.width}; image1.height: ${image1.height}; image2.height: ${image2.height}" }
    assert(image1.height == image2.height) { "Image dimensions doesn't match! image1.width: ${image1.width}; image2.width: ${image2.width}; image1.height: ${image1.height}; image2.height: ${image2.height}" }

    for (x in 0 until image1.width) {
        for (y in 0 until image1.height) {
            assert(image1.getRGB(x, y) == image2.getRGB(x, y)) { "Pixel at ($x, $y) is not the same!" }
        }
    }
}

fun assert(value: Boolean) = assert(value) { "Assertion failed" }

fun assert(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        throw AssertionError(message)
    }
}

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