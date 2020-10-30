package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import loadFromJar
import net.perfectdreams.imagegen.generators.skewed.EdnaldoBandeiraGenerator

class EdnaldoBandeiraGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    "/sources/cat_passion.jpg",
    loadFromJar(EdnaldoBandeiraGenerator::class.java, "/ednaldo_bandeira/template.png"),
    "/templates_check/ednaldo_bandeira.png",
    {
        EdnaldoBandeiraGenerator(it)
    }
)