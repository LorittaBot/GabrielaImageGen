package net.perfectdreams.gabrielaimageserver.webserver.generators

import net.perfectdreams.gabrielaimageserver.generators.*
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
    val chavesOpeningGenerator =
        ChavesOpeningGenerator(tempFolder, File(assetsFolder, "video_templates/chaves"), ffmpegPath, Font.createFont(Font.TRUETYPE_FONT, File(assetsFolder, "fonts/carter-one-regular.ttf")))
    val petPetGenerator = PetPetGenerator(m.gifsicle, File(assetsFolder, "image_templates/hand_pat"))
    val cepoDeMadeiraGenerator = CepoDeMadeiraGenerator(m.gifsicle, File(assetsFolder, "image_templates/cepo"))
    val getOverHereGenerator = GetOverHereGenerator(m.gifsicle, File(assetsFolder, "image_templates/get_over_here"))
    val knucklesThrowGenerator = KnucklesThrowGenerator(m.gifsicle, File(assetsFolder, "image_templates/knux_throw"))
    val nichijouYuukoPaperGenerator =
        NichijouYuukoPaperGenerator(m.gifsicle, File(assetsFolder, "image_templates/nichijou_yuuko_paper"))
    val trumpGenerator = TrumpGenerator(m.gifsicle, File(assetsFolder, "image_templates/trump"))
    val terminatorAnimeGenerator = TerminatorAnimeGenerator(
        loadImageFromImageTemplates("/terminator_anime/template.png"),
        Font.createFont(Font.TRUETYPE_FONT, File(assetsFolder, "fonts/lato-bold.ttf")).deriveFont(24f)
    )
    val samLogo1Generator = SAMGenerator(loadImageFromImageTemplates("/sam/sam_1.png"))
    val samLogo2Generator = SAMGenerator(loadImageFromImageTemplates("/sam/sam_2.png"))
    val samLogo3Generator = SAMGenerator(loadImageFromImageTemplates("/sam/sam_3.png"))

    val shipGenerator = ShipGenerator(
        ShipGenerator.ShipGeneratorAssets(
            loadImageFromImageTemplates("/ship/base_sparkling_hearts.png"),
            loadImageFromImageTemplates("/ship/base_hearts.png"),
            loadImageFromImageTemplates("/ship/base_shrug.png"),
            loadImageFromImageTemplates("/ship/base_sob.png"),
            loadImageFromImageTemplates("/ship/base_skulls.png"),
            loadImageFromImageTemplates("/ship/base_outline.png"),
            loadImageFromImageTemplates("/ship/status_sparkling_heart.png"),
            loadImageFromImageTemplates("/ship/status_heart.png"),
            loadImageFromImageTemplates("/ship/status_shrug.png"),
            loadImageFromImageTemplates("/ship/status_sob.png"),
            loadImageFromImageTemplates("/ship/status_skull.png"),
            loadImageFromImageTemplates("/ship/avatar_wrapper_outline.png"),
            loadImageFromImageTemplates("/ship/avatar_wrapper_background.png"),
            loadImageFromImageTemplates("/ship/progress_bar.png"),
            loadImageFromImageTemplates("/ship/progress_bar_background.png"),
            loadImageFromImageTemplates("/ship/progress_bar_overlay.png"),
            loadImageFromImageTemplates("/ship/progress_bar_reflection.png"),
            loadImageFromImageTemplates("/ship/gabi_resized.png"),
            loadImageFromImageTemplates("/ship/pantufa_resized.png"),
            loadImageFromImageTemplates("/ship/loritta_resized.png"),
            Font.createFont(Font.TRUETYPE_FONT, File(assetsFolder, "fonts/gorditas-bold.ttf"))
        )
    )

    val minecraftSkinLorittaSweatshirtGenerator = MinecraftSkinLorittaSweatshirtGenerator(
        MinecraftSkinLorittaSweatshirtGenerator.MinecraftSkinLorittaSweatshirtGeneratorAssets(
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/classic_light.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/slim_light.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/classic_dark.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/slim_dark.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/classic_mix_wavy.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/slim_mix_wavy.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/classic_mix_wavy_stitches.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/slim_mix_wavy_stitches.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/classic_mix_vertical.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/slim_mix_vertical.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/classic_mix_vertical_stitches.png"),
            loadImageFromImageTemplates("/minecraft_skin_loritta_sweatshirt/slim_mix_vertical_stitches.png")
        )
    )

    val sadRealityGenerator = SadRealityGenerator(
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/pixolletta-8px.ttf")
        ),
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/bebas-neue-regular.ttf")
        )
    )

    val colorInfoGenerator = ColorInfoGenerator(
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/pixolletta-8px.ttf")
        )
    )

    val maniaTitleCardGenerator = ManiaTitleCardGenerator(
        loadImageFromImageTemplates("/mania_title_card/title_card.png"),
        loadImageFromImageTemplates("/mania_title_card/cut_left.png"),
        loadImageFromImageTemplates("/mania_title_card/cut_right.png"),
        File(assetsFolder, "image_templates/mania_title_card/mania_font/")
            .listFiles()
            .filter { it.extension == "png" }
            .map {
                val character = it.nameWithoutExtension.toCharArray().first()
                character to ImageIO.read(it)
            }.toMap()
    )
    val drawnMaskAtendenteGenerator = DrawnMaskAtendenteGenerator(
        loadImageFromImageTemplates("/drawn_mask_atendente/template.png"),
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/komika-hand.ttf")
        )
    )
    val drawnMaskSignGenerator = DrawnMaskSignGenerator(loadImageFromImageTemplates("/drawn_mask_sign/template.png"))
    val drawnMaskWordGenerator = DrawnMaskWordGenerator(
        loadImageFromImageTemplates("/drawn_mask_word/drawn_mask_word.png"),
        loadImageFromImageTemplates("/drawn_mask_word/drawn_mask_word_bottom.png"),
        loadImageFromImageTemplates("/drawn_mask_word/drawn_word.png")
    )
    val toBeContinuedGenerator = ToBeContinuedGenerator(loadImageFromImageTemplates("/to_be_continued/arrow.png"))
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
    val gigachadGenerator = GigaChadGenerator(
        Font.createFont(
            Font.TRUETYPE_FONT,
            File(m.config.assetsFolder + "/fonts/montserrat-extrabold.otf")
        ),
        tempFolder,
        File(assetsFolder, "video_templates/gigachad"),
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
     * Loads a image from the [clazz] in the [path] as a [BufferedImage]
     *
     * @param  path the path inside of the [assetsFolder] where the file is
     * @result      the image
     */
    fun loadImage(path: String) = ImageIO.read(File(m.config.assetsFolder + path))

    /**
     * Loads a image from the [clazz] in the image_templates/[path] as a [BufferedImage]
     *
     * @param  path the path inside of the [assetsFolder] where the file is
     * @result      the image
     */
    fun loadImageFromImageTemplates(path: String) = loadImage("image_templates" + path)

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