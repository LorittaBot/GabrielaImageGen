package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.imagegen.generators.skewed.Bolsonaro2Generator

class Bolsonaro2GeneratorTest : SimpleSkewedImageGeneratorTestBase(
    Bolsonaro2Generator::class.java,
    {
        Bolsonaro2Generator(it)
    }
)