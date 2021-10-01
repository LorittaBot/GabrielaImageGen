package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.ChicoAtaGenerator

class ChicoAtaGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    ChicoAtaGenerator::class.java,
    {
        ChicoAtaGenerator(it)
    }
)