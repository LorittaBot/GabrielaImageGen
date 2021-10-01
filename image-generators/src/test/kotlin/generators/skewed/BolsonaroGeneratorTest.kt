package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.BolsonaroGenerator

class BolsonaroGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BolsonaroGenerator::class.java,
    {
        BolsonaroGenerator(it)
    }
)