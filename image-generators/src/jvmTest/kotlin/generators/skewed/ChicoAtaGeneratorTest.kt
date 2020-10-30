package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.ChicoAtaGenerator

class ChicoAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    ChicoAtaGenerator::class.java,
    {
        ChicoAtaGenerator(it)
    }
)