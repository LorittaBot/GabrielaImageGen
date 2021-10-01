package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.LoriSignGenerator

class LoriSignGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    LoriSignGenerator::class.java,
    {
        LoriSignGenerator(it)
    }
)