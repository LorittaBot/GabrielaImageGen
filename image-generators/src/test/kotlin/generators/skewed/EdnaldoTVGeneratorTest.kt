package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import loadFromJar
import net.perfectdreams.gabrielaimageserver.generators.skewed.EdnaldoTVGenerator

class EdnaldoTVGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    "/sources/cat_passion.jpg",
    loadFromJar(EdnaldoTVGenerator::class.java, "/image_templates/ednaldo_tv/template.png"),
    "/templates_check/ednaldo_tv.png",
    {
        EdnaldoTVGenerator(it)
    }
)