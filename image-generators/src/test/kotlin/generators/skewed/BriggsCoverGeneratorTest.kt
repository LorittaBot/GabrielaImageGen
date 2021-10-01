package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.BriggsCoverGenerator

class BriggsCoverGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    BriggsCoverGenerator::class.java,
    {
        BriggsCoverGenerator(it)
    }
)