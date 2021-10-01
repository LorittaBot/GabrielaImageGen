package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.RipTvGenerator

class RipTvGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    RipTvGenerator::class.java,
    {
        RipTvGenerator(it)
    }
)