package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.BobBurningPaperGenerator

class BobBurningPaperGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BobBurningPaperGenerator::class.java,
    {
        BobBurningPaperGenerator(it)
    }
)