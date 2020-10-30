package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.CanellaDVDGenerator

class CanellaDVDGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    CanellaDVDGenerator::class.java,
    {
        CanellaDVDGenerator(it)
    }
)