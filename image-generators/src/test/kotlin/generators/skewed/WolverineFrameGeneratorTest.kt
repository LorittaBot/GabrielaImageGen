package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.WolverineFrameGenerator

class WolverineFrameGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    WolverineFrameGenerator::class.java,
    {
        WolverineFrameGenerator(it)
    }
)