package generators.scaled

import SimpleScaledImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.scaled.PepeDreamGenerator

class PepeDreamGeneratorTest : SimpleScaledImageGeneratorTestBase(
    PepeDreamGenerator::class.java,
    {
        PepeDreamGenerator(it)
    }
)