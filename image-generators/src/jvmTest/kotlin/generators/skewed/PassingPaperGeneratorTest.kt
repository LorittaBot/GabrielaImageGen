package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.PassingPaperGenerator

class PassingPaperGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    PassingPaperGenerator::class.java,
    {
        PassingPaperGenerator(it)
    }
)