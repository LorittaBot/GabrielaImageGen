package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.GessyAtaGenerator

class GessyAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    GessyAtaGenerator::class.java,
    {
        GessyAtaGenerator(it)
    }
)