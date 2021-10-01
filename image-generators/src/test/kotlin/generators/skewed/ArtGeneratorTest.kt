package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.ArtGenerator

class ArtGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    ArtGenerator::class.java,
    {
        ArtGenerator(it)
    }
)