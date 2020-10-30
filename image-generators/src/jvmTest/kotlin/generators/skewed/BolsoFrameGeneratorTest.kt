package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.BolsoFrameGenerator

class BolsoFrameGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BolsoFrameGenerator::class.java,
    {
        BolsoFrameGenerator(it)
    }
)