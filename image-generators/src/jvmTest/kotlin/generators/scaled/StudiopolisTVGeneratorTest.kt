package generators.scaled

import SimpleScaledImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.scaled.StudiopolisTVGenerator

class StudiopolisTVGeneratorTest : SimpleScaledImageGeneratorTestBase(
    StudiopolisTVGenerator::class.java,
    {
        StudiopolisTVGenerator(it)
    }
)