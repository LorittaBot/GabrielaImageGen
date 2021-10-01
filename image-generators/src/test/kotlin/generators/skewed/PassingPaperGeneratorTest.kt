package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.PassingPaperGenerator

class PassingPaperGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    PassingPaperGenerator::class.java,
    {
        PassingPaperGenerator(it)
    }
)