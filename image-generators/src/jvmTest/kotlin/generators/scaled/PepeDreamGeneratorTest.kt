package generators.scaled

import SimpleScaledImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.scaled.PepeDreamGenerator

class PepeDreamGeneratorTest : SimpleScaledImageGeneratorTestBase(
    PepeDreamGenerator::class.java,
    {
        PepeDreamGenerator(it)
    }
)