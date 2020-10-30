package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.BolsonaroGenerator

class BolsonaroGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BolsonaroGenerator::class.java,
    {
        BolsonaroGenerator(it)
    }
)