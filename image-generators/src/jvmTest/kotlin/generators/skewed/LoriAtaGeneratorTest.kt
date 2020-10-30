package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.LoriAtaGenerator

class LoriAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    LoriAtaGenerator::class.java,
    {
        LoriAtaGenerator(it)
    }
)