package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.WolverineFrameGenerator

class WolverineFrameGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    WolverineFrameGenerator::class.java,
    {
        WolverineFrameGenerator(it)
    }
)