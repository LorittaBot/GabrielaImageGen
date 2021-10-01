
import net.perfectdreams.gabrielaimageserver.generators.BasicScaledImageGenerator
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

abstract class SimpleScaledImageGeneratorTestBase(
    val sourcePath: String,
    val templateInputStream: InputStream,
    val resultPath: String,
    val generatorBuilder: ((BufferedImage) -> BasicScaledImageGenerator)
) {
    constructor(clazz: Class<*>, callback: ((BufferedImage) -> BasicScaledImageGenerator)) : this(
        "/sources/cat_passion.jpg",
        loadFromJar(clazz, "/image_templates/${convertToSnakeCase(clazz.simpleName)}/template.png"),
        "/templates_check/${convertToSnakeCase(clazz.simpleName)}.png",
        callback
    )

    @Test
    fun test() {
        val source = ImageIO.read(loadFromJar(SimpleScaledImageGeneratorTestBase::class.java, sourcePath))
        val templateResult = ImageIO.read(loadFromJar(SimpleScaledImageGeneratorTestBase::class.java, resultPath))

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