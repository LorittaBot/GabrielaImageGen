package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.BobBurningPaperGenerator

class BobBurningPaperGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BobBurningPaperGenerator::class.java,
    {
        BobBurningPaperGenerator(it)
    }
)