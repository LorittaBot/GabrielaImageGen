package generators.scaled

import SimpleScaledImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.scaled.LoriScaredGenerator

class LoriScaredGeneratorTest : SimpleScaledImageGeneratorTestBase(
    LoriScaredGenerator::class.java,
    {
        LoriScaredGenerator(it)
    }
)