package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.LoriSignGenerator

class LoriSignGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    LoriSignGenerator::class.java,
    {
        LoriSignGenerator(it)
    }
)