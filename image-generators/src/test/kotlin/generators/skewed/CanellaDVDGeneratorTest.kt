package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.CanellaDVDGenerator

class CanellaDVDGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    CanellaDVDGenerator::class.java,
    {
        CanellaDVDGenerator(it)
    }
)