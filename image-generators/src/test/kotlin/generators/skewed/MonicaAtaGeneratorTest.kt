package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.MonicaAtaGenerator

class MonicaAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    MonicaAtaGenerator::class.java,
    {
        MonicaAtaGenerator(it)
    }
)