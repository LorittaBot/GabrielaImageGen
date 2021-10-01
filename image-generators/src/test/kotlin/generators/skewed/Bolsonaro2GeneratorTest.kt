package generators.skewed

import SimpleSkewedImageGeneratorTestBase
import net.perfectdreams.gabrielaimageserver.generators.skewed.Bolsonaro2Generator

class Bolsonaro2GeneratorTest : SimpleSkewedImageGeneratorTestBase(
    Bolsonaro2Generator::class.java,
    {
        Bolsonaro2Generator(it)
    }
)