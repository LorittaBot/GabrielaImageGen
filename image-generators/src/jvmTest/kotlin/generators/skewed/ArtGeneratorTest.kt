package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.ArtGenerator

class ArtGeneratorTest : SimpleSkewedImageGeneratorTestBase(
    ArtGenerator::class.java,
    {
        ArtGenerator(it)
    }
)