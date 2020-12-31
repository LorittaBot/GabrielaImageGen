package net.perfectdreams.imageserver.generators

import net.perfectdreams.imagegen.cortesflow.*
import net.perfectdreams.imagegen.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.imagegen.generators.drake.DrakeGenerator
import net.perfectdreams.imagegen.generators.drake.LoriDrakeGenerator
import net.perfectdreams.imagegen.generators.scaled.LoriScaredGenerator
import net.perfectdreams.imagegen.generators.scaled.PepeDreamGenerator
import net.perfectdreams.imagegen.generators.scaled.StudiopolisTVGenerator
import net.perfectdreams.imagegen.generators.skewed.*
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.convertToSnakeCase
import java.awt.Font
import java.io.File
import javax.imageio.ImageIO

class Generators(val m: GabrielaImageGen) {
    private val tempFolder = File(m.config.tempFolder)
    private val assetsFolder = File(m.config.assetsFolder)
    private val ffmpegPath = File(m.config.ffmpegPath)

    init {
        tempFolder.mkdirs()
        assetsFolder.mkdirs()
    }

    val CARLY_AAAH_GENERATOR = CarlyAaahGenerator(tempFolder, File(assetsFolder, "carly_aaah"), ffmpegPath)
    val HAND_PAT_GENERATOR = PetPetGenerator(m, File(assetsFolder, "hand_pat"))

    // ===[ SKEWED IMAGE GENERATORS ]===
    val artGenerator = createSimpleSkewedGenerator<ArtGenerator>()
    val bobBurningPaperGenerator = createSimpleSkewedGenerator<BobBurningPaperGenerator>()
    val bolsoFrameGenerator = createSimpleSkewedGenerator<BolsoFrameGenerator>()
    val bolsonaroGenerator = createSimpleSkewedGenerator<BolsonaroGenerator>()
    val bolsonaro2Generator = createSimpleSkewedGenerator<Bolsonaro2Generator>()
    val briggsCoverGenerator = createSimpleSkewedGenerator<BriggsCoverGenerator>()
    val buckShirtGenerator = createSimpleSkewedGenerator<BuckShirtGenerator>()
    val canellaDVDGenerator = createSimpleSkewedGenerator<CanellaDVDGenerator>()
    val chicoAtaGenerator = createSimpleSkewedGenerator<ChicoAtaGenerator>()
    val ednaldoBandeiraGenerator = createSimpleSkewedGenerator<EdnaldoBandeiraGenerator>()
    val ednaldoTVGenerator = createSimpleSkewedGenerator<EdnaldoTVGenerator>()
    val gessyAtaGenerator = createSimpleSkewedGenerator<GessyAtaGenerator>()
    val loriAtaGenerator = createSimpleSkewedGenerator<LoriAtaGenerator>()
    val loriSignGenerator = createSimpleSkewedGenerator<LoriSignGenerator>()
    val monicaAtaGenerator = createSimpleSkewedGenerator<MonicaAtaGenerator>()
    val passingPaperGenerator = createSimpleSkewedGenerator<PassingPaperGenerator>()
    val romeroBrittoGenerator = createSimpleSkewedGenerator<RomeroBrittoGenerator>()
    val wolverineFrameGenerator = createSimpleSkewedGenerator<WolverineFrameGenerator>()

    // ===[ SCALED IMAGE GENERATORS ]===
    val loriScaredGenerator = createSimpleScaledGenerator<LoriScaredGenerator>()
    val pepeDreamGenerator = createSimpleScaledGenerator<PepeDreamGenerator>()
    val studiopolisTVGenerator = createSimpleScaledGenerator<StudiopolisTVGenerator>()

    // ===[ DRAKE GENERATORS ]===
    val drakeGenerator = createSimpleDrakeGenerator<DrakeGenerator>()
    val loriDrakeGenerator = createSimpleDrakeGenerator<LoriDrakeGenerator>()
    val bolsoDrakeGenerator = createSimpleDrakeGenerator<BolsoDrakeGenerator>()

    // ===[ CORTES FLOW GENERATORS ]===
    val cortesFlowGenerators = CortesFlowGenerators()

    /**
     * Loads a image from the [clazz] in the [path] as a [JVMImage]
     *
     * @param  path the path inside of the [assetsFolder] where the file is
     * @result      the image
     */
    fun loadImage(clazz: Class<*>, path: String) = JVMImage(ImageIO.read(clazz.getResourceAsStream(path)))

    inline fun <reified T> createSimpleSkewedGenerator() = T::class.constructors.first().call(
        loadImage(
            T::class.java,
            "/image_templates/${convertToSnakeCase(T::class.simpleName!!)}/template.png"
        )
    ) as T

    inline fun <reified T> createSimpleScaledGenerator() = T::class.constructors.first().call(
        loadImage(
            T::class.java,
            "/image_templates/${convertToSnakeCase(T::class.simpleName!!)}/template.png"
        )
    ) as T

    inline fun <reified T> createSimpleDrakeGenerator() = T::class.constructors.first().call(
        loadImage(
            T::class.java,
            "/image_templates/${convertToSnakeCase(T::class.simpleName!!)}/template.png"
        )
    )
}