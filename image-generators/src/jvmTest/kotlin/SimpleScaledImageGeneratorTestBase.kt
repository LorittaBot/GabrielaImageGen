import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

abstract class SimpleScaledImageGeneratorTestBase(
    val sourcePath: String,
    val templateInputStream: InputStream,
    val resultPath: String,
    val generatorBuilder: ((Image) -> BasicScaledImageGenerator)
) {
    constructor(clazz: Class<*>, callback: ((Image) -> BasicScaledImageGenerator)) : this(
        "/sources/cat_passion.jpg",
        loadFromJar(clazz, "/image_templates/${convertToSnakeCase(clazz.simpleName)}/template.png"),
        "/templates_check/${convertToSnakeCase(clazz.simpleName)}.png",
        callback
    )

    @Test
    fun test() {
        val source = JVMImage(ImageIO.read(loadFromJar(SimpleScaledImageGeneratorTestBase::class.java, sourcePath)))
        val templateResult = JVMImage(ImageIO.read(loadFromJar(SimpleScaledImageGeneratorTestBase::class.java, resultPath)))

        val result = generatorBuilder.invoke(
            JVMImage(
                ImageIO.read(
                    templateInputStream
                )
            )
        ).generate(source)

        checkIfImagesAreEqual(
            templateResult.handle as BufferedImage,
            (result as JVMImage)
                .handle as BufferedImage
        )
    }
}