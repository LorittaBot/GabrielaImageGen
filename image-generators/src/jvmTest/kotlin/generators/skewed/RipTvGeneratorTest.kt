package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.BriggsCoverGenerator
import net.perfectdreams.imagegen.generators.skewed.RipTvGenerator

class RipTvGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    RipTvGenerator::class.java,
    {
        RipTvGenerator(it)
    }
)