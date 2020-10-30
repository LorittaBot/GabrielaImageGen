package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.MonicaAtaGenerator

class MonicaAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    MonicaAtaGenerator::class.java,
    {
        MonicaAtaGenerator(it)
    }
)