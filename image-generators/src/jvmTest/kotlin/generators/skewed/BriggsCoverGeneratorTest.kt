package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.BriggsCoverGenerator

class BriggsCoverGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BriggsCoverGenerator::class.java,
    {
        BriggsCoverGenerator(it)
    }
)