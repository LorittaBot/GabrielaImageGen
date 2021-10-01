package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.BolsoFrameGenerator

class BolsoFrameGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BolsoFrameGenerator::class.java,
    {
        BolsoFrameGenerator(it)
    }
)