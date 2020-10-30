package generators.scaled

import SimpleScaledImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.scaled.LoriScaredGenerator

class LoriScaredGeneratorTest : SimpleScaledImageGeneratorTestBase(
    LoriScaredGenerator::class.java,
    {
        LoriScaredGenerator(it)
    }
)