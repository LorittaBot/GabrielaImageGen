package net.perfectdreams.gabrielaimageserver.webserver.generators

import net.perfectdreams.gabrielaimageserver.generators.AttackOnHeartGenerator
import net.perfectdreams.gabrielaimageserver.generators.CarlyAaahGenerator
import net.perfectdreams.gabrielaimageserver.generators.CepoDeMadeiraGenerator
import net.perfectdreams.gabrielaimageserver.generators.CocieloChavesGenerator
import net.perfectdreams.gabrielaimageserver.generators.DrawnMaskAtendenteGenerator
import net.perfectdreams.gabrielaimageserver.generators.DrawnMaskSignGenerator
import net.perfectdreams.gabrielaimageserver.generators.DrawnMaskWordGenerator
import net.perfectdreams.gabrielaimageserver.generators.FansExplainingGenerator
import net.perfectdreams.gabrielaimageserver.generators.GetOverHereGenerator
import net.perfectdreams.gabrielaimageserver.generators.InvertColorsGenerator
import net.perfectdreams.gabrielaimageserver.generators.KnucklesThrowGenerator
import net.perfectdreams.gabrielaimageserver.generators.ManiaTitleCardGenerator
import net.perfectdreams.gabrielaimageserver.generators.MemeMakerGenerator
import net.perfectdreams.gabrielaimageserver.generators.MinecraftSkinLorittaSweatshirtGenerator
import net.perfectdreams.gabrielaimageserver.generators.NichijouYuukoPaperGenerator
import net.perfectdreams.gabrielaimageserver.generators.PetPetGenerator
import net.perfectdreams.gabrielaimageserver.generators.SAMGenerator
import net.perfectdreams.gabrielaimageserver.generators.SadRealityGenerator
import net.perfectdreams.gabrielaimageserver.generators.ShipGenerator
import net.perfectdreams.gabrielaimageserver.generators.TerminatorAnimeGenerator
import net.perfectdreams.gabrielaimageserver.generators.ToBeContinuedGenerator
import net.perfectdreams.gabrielaimageserver.generators.TrumpGenerator
import net.perfectdreams.gabrielaimageserver.generators.drake.BolsoDrakeGenerator
import net.perfectdreams.gabrielaimageserver.generators.drake.DrakeGenerator
import net.perfectdreams.gabrielaimageserver.generators.drake.LoriDrakeGenerator
import net.perfectdreams.gabrielaimageserver.generators.scaled.LoriScaredGenerator
import net.perfectdreams.gabrielaimageserver.generators.scaled.MarkMetaGenerator
import net.perfectdreams.gabrielaimageserver.generators.scaled.PepeDreamGenerator
import net.perfectdreams.gabrielaimageserver.generators.scaled.StudiopolisTVGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.ArtGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.BobBurningPaperGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.BolsoFrameGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.Bolsonaro2Generator
import net.perfectdreams.gabrielaimageserver.generators.skewed.BolsonaroGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.BriggsCoverGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.BuckShirtGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.CanellaDVDGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.ChicoAtaGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.EdnaldoBandeiraGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.EdnaldoTVGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.GessyAtaGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.LoriAtaGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.LoriSignGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.MonicaAtaGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.PassingPaperGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.RipTvGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.RomeroBrittoGenerator
import net.perfectdreams.gabrielaimageserver.generators.skewed.WolverineFrameGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
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

    val carlyAaahGenerator =
        CarlyAaahGenerator(tempFolder, File(assetsFolder, "video_templates/carly_aaah"), ffmpegPath)
    val attackOnHeartGenerator =
        AttackOnHeartGenerator(tempFolder, File(assetsFolder, "video_templates/attack_on_heart"), ffmpegPath)
    val cocieloChavesGenerator =
        CocieloChavesGenerator(tempFolder, File(assetsFolder, "video_templates/cocielo_chaves"), ffmpegPath)
    val petPetGenerator = PetPetGenerator(m.gifsicle, File(assetsFolder, "image_templates/hand_pat"))
    val cepoDeMadeiraGenerator = CepoDeMadeiraGenerator(m.gifsicle, File(assetsFolder, "image_templates/cepo"))
    val getOverHereGenerator = GetOverHereGenerator(m.gifsicle, File(assetsFolder, "image_templates/get_over_here"))
    val knucklesThrowGenerator = KnucklesThrowGenerator(m.gifsicle, File(assetsFolder, "image_templates/knux_throw"))
    val nichijouYuukoPaperGenerator =
        NichijouYuukoPaperGenerator(m.gifsicle, File(assetsFolder, "image_templates/nichijou_yuuko_paper"))
    val trumpGenerator = TrumpGenerator(m.gifsicle, File(assetsFolder, "image_templates/trump"))
    val terminatorAnimeGenerator = TerminatorAnimeGenerator(
        ImageIO.read(File(assetsFolder, "image_templates/terminator_anime/template.png")),
        Font.createFont(Font.TRUETYPE_FONT, File(assetsFolder, "fonts/lato-bold.ttf")).deriveFont(24f)
    )
    val samLogo1Generator = SAMGenerator(loadImage("image_templates/sam/sam_1.png"))
    val samLogo2Generator = SAMGenerator(loadImage("image_templates/sam/sam_2.png"))
    val samLogo3Generator = SAMGenerator(loadImage("image_templates/sam/sam_3.png"))

    val shipGenerator = ShipGenerator(
        ShipGenerator.ShipGeneratorAssets(
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/base_sparkling_hearts.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/base_hearts.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/base_shrug.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/base_sob.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/base_skulls.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/base_outline.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/status_sparkling_heart.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/status_heart.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/status_shrug.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/status_sob.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/status_skull.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/avatar_wrapper_outline.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/avatar_wrapper_background.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/progress_bar.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/progress_bar_background.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/progress_bar_overlay.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/progress_bar_reflection.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/gabi_resized.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/pantufa_resized.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/ship/loritta_resized.png")),
            Font.createFont(Font.TRUETYPE_FONT, File(assetsFolder, "fonts/gorditas-bold.ttf"))
        )
    )

    val minecraftSkinLorittaSweatshirtGenerator = MinecraftSkinLorittaSweatshirtGenerator(
        MinecraftSkinLorittaSweatshirtGenerator.MinecraftSkinLorittaSweatshirtGeneratorAssets(
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/classic_light.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/slim_light.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/classic_dark.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/slim_dark.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/classic_mix_wavy.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/slim_mix_wavy.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/classic_mix_wavy_stitches.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/slim_mix_wavy_stitches.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/classic_mix_vertical.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/slim_mix_vertical.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/classic_mix_vertical_stitches.png")),
            ImageIO.read(File(m.config.assetsFolder + "image_templates/minecraft_skin_loritta_sweatshirt/slim_mix_vertical_stitches.png"))
        )
    )

    val sadRealityGenerator = SadRealityGenerator(
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/bebas-neue-regular.ttf")
        )
    )

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
    val drawnMaskAtendenteGenerator = DrawnMaskAtendenteGenerator(
        ImageIO.read(File(assetsFolder, "image_templates/drawn_mask_atendente/template.png")),
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/komika-hand.ttf")
        )
    )
    val drawnMaskSignGenerator = DrawnMaskSignGenerator(
        ImageIO.read(File(assetsFolder, "image_templates/drawn_mask_sign/template.png"))
    )
    val drawnMaskWordGenerator = DrawnMaskWordGenerator(
        ImageIO.read(File(assetsFolder, "image_templates/drawn_mask_word/drawn_mask_word.png")),
        ImageIO.read(File(assetsFolder, "image_templates/drawn_mask_word/drawn_mask_word_bottom.png")),
        ImageIO.read(File(assetsFolder, "image_templates/drawn_mask_word/drawn_word.png"))
    )
    val toBeContinuedGenerator = ToBeContinuedGenerator(
        ImageIO.read(File(assetsFolder, "image_templates/to_be_continued/arrow.png"))
    )
    val invertColorsGenerator = InvertColorsGenerator()
    val fansExplainingGenerator = FansExplainingGenerator(
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/montserrat-extrabold.otf")
        ),
        tempFolder,
        File(assetsFolder, "video_templates/fans_explaining"),
        ffmpegPath
    )
    val memeMakerGenerator = MemeMakerGenerator(
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/montserrat-extrabold.otf")
        )
    )

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
    val markMetaGenerator = createSimpleScaledGenerator<MarkMetaGenerator>()

    // ===[ DRAKE GENERATORS ]===
    val drakeGenerator = createSimpleDrakeGenerator<DrakeGenerator>()
    val loriDrakeGenerator = createSimpleDrakeGenerator<LoriDrakeGenerator>()
    val bolsoDrakeGenerator = createSimpleDrakeGenerator<BolsoDrakeGenerator>()

    // ===[ CORTES FLOW GENERATORS ]===
    val cortesFlowGenerators = CortesFlowGenerators(m)

    // ===[ TOBY FOX TEXT BOX GENERATOR ]===
    val tobyTextBoxGenerators = TobyTextBoxGenerators(this)

    /**
     * Loads a image from the [clazz] in the [path] as a [JVMImage]
     *
     * @param  path the path inside of the [assetsFolder] where the file is
     * @result      the image
     */
    fun loadImage(path: String) = ImageIO.read(File(m.config.assetsFolder + path))

    /**
     * Loads a image from the [clazz] in the [path] as a [BufferedImage]
     *
     * @param  path the path inside of the [assetsFolder] where the file is
     * @result      the image
     */
    fun loadBufferedImage(path: String) = ImageIO.read(File(m.config.assetsFolder + path))

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