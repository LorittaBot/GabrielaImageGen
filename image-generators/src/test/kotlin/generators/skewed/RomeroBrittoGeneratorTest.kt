package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.RomeroBrittoGenerator

class RomeroBrittoGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    RomeroBrittoGenerator::class.java,
    {
        RomeroBrittoGenerator(it)
    }
)