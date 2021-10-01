
import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

abstract class SimpleSkewedImageGeneratorTestBase(
    val sourcePath: String,
    val templateInputStream: InputStream,
    val resultPath: String,
    val generatorBuilder: ((BufferedImage) -> BasicSkewedImageGenerator)
) {
    constructor(clazz: Class<*>, callback: ((BufferedImage) -> BasicSkewedImageGenerator)) : this(
        "/sources/cat_passion.jpg",
        loadFromJar(clazz, "/image_templates/${convertToSnakeCase(clazz.simpleName)}/template.png"),
        "/templates_check/${convertToSnakeCase(clazz.simpleName)}.png",
        callback
    )

    @Test
    fun test() {
        val source = ImageIO.read(loadFromJar(SimpleSkewedImageGeneratorTestBase::class.java, sourcePath))
        val templateResult = ImageIO.read(loadFromJar(SimpleSkewedImageGeneratorTestBase::class.java, resultPath))

        val result = generatorBuilder.invoke(
            ImageIO.read(
                templateInputStream
            )
        ).generateImage(source)

        checkIfImagesAreEqual(
            templateResult,
            result
        )
    }
}