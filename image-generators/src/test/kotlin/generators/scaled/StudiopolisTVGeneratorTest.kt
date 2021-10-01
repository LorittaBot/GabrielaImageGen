package generators.scaled

import SimpleScaledImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.scaled.StudiopolisTVGenerator

class StudiopolisTVGeneratorTest : SimpleScaledImageGeneratorTestBase(
    StudiopolisTVGenerator::class.java,
    {
        StudiopolisTVGenerator(it)
    }
)