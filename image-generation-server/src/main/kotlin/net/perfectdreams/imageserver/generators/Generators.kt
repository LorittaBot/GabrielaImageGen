package net.perfectdreams.imageserver.generators

import net.perfectdreams.imagegen.generators.AttackOnHeartGenerator
import net.perfectdreams.imagegen.generators.CarlyAaahGenerator
import net.perfectdreams.imagegen.generators.CepoDeMadeiraGenerator
import net.perfectdreams.imagegen.generators.CocieloChavesGenerator
import net.perfectdreams.imagegen.generators.CortesFlowGenerators
import net.perfectdreams.imagegen.generators.GetOverHereGenerator
import net.perfectdreams.imagegen.generators.InvertColorsGenerator
import net.perfectdreams.imagegen.generators.KnucklesThrowGenerator
import net.perfectdreams.imagegen.generators.ManiaTitleCardGenerator
import net.perfectdreams.imagegen.generators.NichijouYuukoPaperGenerator
import net.perfectdreams.imagegen.generators.PetPetGenerator
import net.perfectdreams.imagegen.generators.SAMGenerator
import net.perfectdreams.imagegen.generators.TerminatorAnimeGenerator
import net.perfectdreams.imagegen.generators.ToBeContinuedGenerator
import net.perfectdreams.imagegen.generators.TrumpGenerator
import net.perfectdreams.imagegen.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.imagegen.generators.drake.DrakeGenerator
import net.perfectdreams.imagegen.generators.drake.LoriDrakeGenerator
import net.perfectdreams.imagegen.generators.scaled.LoriScaredGenerator
import net.perfectdreams.imagegen.generators.scaled.PepeDreamGenerator
import net.perfectdreams.imagegen.generators.scaled.StudiopolisTVGenerator
import net.perfectdreams.imagegen.generators.skewed.ArtGenerator
import net.perfectdreams.imagegen.generators.skewed.BobBurningPaperGenerator
import net.perfectdreams.imagegen.generators.skewed.BolsoFrameGenerator
import net.perfectdreams.imagegen.generators.skewed.Bolsonaro2Generator
import net.perfectdreams.imagegen.generators.skewed.BolsonaroGenerator
import net.perfectdreams.imagegen.generators.skewed.BriggsCoverGenerator
import net.perfectdreams.imagegen.generators.skewed.BuckShirtGenerator
import net.perfectdreams.imagegen.generators.skewed.CanellaDVDGenerator
import net.perfectdreams.imagegen.generators.skewed.ChicoAtaGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoBandeiraGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoTVGenerator
import net.perfectdreams.imagegen.generators.skewed.GessyAtaGenerator
import net.perfectdreams.imagegen.generators.skewed.LoriAtaGenerator
import net.perfectdreams.imagegen.generators.skewed.LoriSignGenerator
import net.perfectdreams.imagegen.generators.skewed.MonicaAtaGenerator
import net.perfectdreams.imagegen.generators.skewed.PassingPaperGenerator
import net.perfectdreams.imagegen.generators.skewed.RipTvGenerator
import net.perfectdreams.imagegen.generators.skewed.RomeroBrittoGenerator
import net.perfectdreams.imagegen.generators.skewed.WolverineFrameGenerator
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import java.awt.Font
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class Generators(val m: GabrielaImageGen) {
    companion object {
        fun generateFileName(name: String, extension: String) =
            "$name-${System.currentTimeMillis()}-${UUID.randomUUID()}.$extension"

        fun readBytesAndDelete(file: File): ByteArray {
            val bytes = file.readBytes()
            file.delete()
            return bytes
        }

        /**
         * Converts the [input] class name to snake_case, also removes the suffixes "Test", "CortesFlow" and "Generator"
         *
         * @param input the class name that will be converted to snake_case
         * @return the class name in snake_case
         */
        fun convertToSnakeCase(input: String): String {
            val x = input.removeSuffix("Test").removeSuffix("Generator").removeSuffix("CortesFlow")

            val newString = StringBuilder()

            for (index in x.indices) {
                val charAt = x[index]
                val nextChar = x.getOrNull(index + 1)

                if (charAt.isLowerCase() && nextChar?.isUpperCase() == true) {
                    newString.append(charAt.toLowerCase())
                    newString.append("_")
                } else {
                    newString.append(charAt.toLowerCase())
                }
            }

            return newString.toString()
        }
    }

    private val tempFolder = File(m.config.tempFolder)
    private val assetsFolder = File(m.config.assetsFolder)
    private val ffmpegPath = File(m.config.ffmpegPath)

    init {
        tempFolder.mkdirs()
        assetsFolder.mkdirs()
    }

    val carlyAaahGenerator = CarlyAaahGenerator(tempFolder, File(assetsFolder, "video_templates/carly_aaah"), ffmpegPath)
    val attackOnHeartGenerator = AttackOnHeartGenerator(tempFolder, File(assetsFolder, "video_templates/attack_on_heart"), ffmpegPath)
    val cocieloChavesGenerator = CocieloChavesGenerator(tempFolder, File(assetsFolder, "video_templates/cocielo_chaves"), ffmpegPath)
    val petPetGenerator = PetPetGenerator(m, File(assetsFolder, "image_templates/hand_pat"))
    val cepoDeMadeiraGenerator = CepoDeMadeiraGenerator(m, File(assetsFolder, "image_templates/cepo"))
    val getOverHereGenerator = GetOverHereGenerator(m, File(assetsFolder, "image_templates/get_over_here"))
    val knucklesThrowGenerator = KnucklesThrowGenerator(m, File(assetsFolder, "image_templates/knux_throw"))
    val nichijouYuukoPaperGenerator = NichijouYuukoPaperGenerator(m, File(assetsFolder, "image_templates/nichijou_yuuko_paper"))
    val trumpGenerator = TrumpGenerator(m, File(assetsFolder, "image_templates/trump"))
    val terminatorAnimeGenerator = TerminatorAnimeGenerator(
        m,
        ImageIO.read(File(assetsFolder, "image_templates/terminator_anime/template.png")),
        Font.createFont(Font.TRUETYPE_FONT, File(assetsFolder, "fonts/lato-bold.ttf")).deriveFont(24f)
    )
    val samLogo1Generator = SAMGenerator(loadImage("image_templates/sam/sam_1.png"))
    val samLogo2Generator = SAMGenerator(loadImage("image_templates/sam/sam_2.png"))
    val samLogo3Generator = SAMGenerator(loadImage("image_templates/sam/sam_3.png"))

    val maniaTitleCardGenerator = ManiaTitleCardGenerator(
        ImageIO.read(File(assetsFolder, "image_templates/mania_title_card/title_card.png")),
        ImageIO.read(File(assetsFolder, "image_templates/mania_title_card/cut_left.png")),
        ImageIO.read(File(assetsFolder, "image_templates/mania_title_card/cut_right.png")),
        File(assetsFolder, "image_templates/mania_title_card/mania_font/")
            .listFiles()
            .filter { it.extension == "png" }
            .map {
                val character = it.nameWithoutExtension.toCharArray().first()
                character to ImageIO.read(it)
            }.toMap()
    )
    val toBeContinuedGenerator = ToBeContinuedGenerator(
        m,
        ImageIO.read(File(assetsFolder, "image_templates/to_be_continued/arrow.png"))
    )
    val invertColorsGenerator = InvertColorsGenerator()

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
    val ripTvGenerator = createSimpleSkewedGenerator<RipTvGenerator>()

    // ===[ SCALED IMAGE GENERATORS ]===
    val loriScaredGenerator = createSimpleScaledGenerator<LoriScaredGenerator>()
    val pepeDreamGenerator = createSimpleScaledGenerator<PepeDreamGenerator>()
    val studiopolisTVGenerator = createSimpleScaledGenerator<StudiopolisTVGenerator>()

    // ===[ DRAKE GENERATORS ]===
    val drakeGenerator = createSimpleDrakeGenerator<DrakeGenerator>()
    val loriDrakeGenerator = createSimpleDrakeGenerator<LoriDrakeGenerator>()
    val bolsoDrakeGenerator = createSimpleDrakeGenerator<BolsoDrakeGenerator>()

    // ===[ CORTES FLOW GENERATORS ]===
    val cortesFlowGenerators = CortesFlowGenerators(m)

    /**
     * Loads a image from the [clazz] in the [path] as a [JVMImage]
     *
     * @param  path the path inside of the [assetsFolder] where the file is
     * @result      the image
     */
    fun loadImage(path: String) = JVMImage(ImageIO.read(File(m.config.assetsFolder + path)))

    inline fun <reified T> createSimpleSkewedGenerator() = T::class.constructors.first().call(
        loadImage(
            "/image_templates/${convertToSnakeCase(T::class.simpleName!!)}/template.png"
        )
    )

    inline fun <reified T> createSimpleScaledGenerator() = T::class.constructors.first().call(
        loadImage(
            "/image_templates/${convertToSnakeCase(T::class.simpleName!!)}/template.png"
        )
    )

    inline fun <reified T> createSimpleDrakeGenerator() = T::class.constructors.first().call(
        loadImage(
            "/image_templates/${convertToSnakeCase(T::class.simpleName!!)}/template.png"
        )
    )
}