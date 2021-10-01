package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.LoriAtaGenerator

class LoriAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    LoriAtaGenerator::class.java,
    {
        LoriAtaGenerator(it)
    }
)