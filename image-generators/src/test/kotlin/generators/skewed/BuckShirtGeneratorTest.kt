package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.BuckShirtGenerator

class BuckShirtGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BuckShirtGenerator::class.java,
    {
        BuckShirtGenerator(it)
    }
)